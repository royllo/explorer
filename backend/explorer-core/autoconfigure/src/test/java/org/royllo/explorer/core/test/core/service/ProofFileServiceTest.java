package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.proof.ProofFileDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.proof.ProofFileService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.royllo.explorer.core.util.exceptions.proof.ProofCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_ID;
import static org.royllo.test.TestAssets.ACTIVE_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TestAssets.ACTIVE_ROYLLO_COIN_PROOF_1_RAW_PROOF;
import static org.royllo.test.TestAssets.ACTIVE_ROYLLO_COIN_PROOF_2_RAW_PROOF;
import static org.royllo.test.TestAssets.ACTIVE_ROYLLO_COIN_PROOF_3_RAW_PROOF;
import static org.royllo.test.TestAssets.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TestAssets.ROYLLO_COIN_PROOF_ID;
import static org.royllo.test.TestAssets.ROYLLO_COIN_RAW_PROOF;
import static org.royllo.test.TestAssets.UNKNOWN_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TestAssets.UNKNOWN_ROYLLO_COIN_RAW_PROOF;
import static org.royllo.test.TestAssets.UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID;

@SpringBootTest
@DisplayName("ProofService tests")
public class ProofFileServiceTest extends TestWithMockServers {

    @Autowired
    private TapdService TAPDService;

    @Autowired
    private ProofFileService proofFileService;

    @Autowired
    private AssetService assetService;

    @Test
    @DisplayName("addProof()")
    public void addProof() {
        // =============================================================================================================
        // Unknown Royllo coin.
        DecodedProofResponse unknownRoylloCoinDecodedProof = TAPDService.decode(UNKNOWN_ROYLLO_COIN_RAW_PROOF).block();
        assertNotNull(unknownRoylloCoinDecodedProof);

        // We add our proof but our an asset doesn't exist yet --> an error must occur.
        assertFalse(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        ProofCreationException e = assertThrows(ProofCreationException.class, () -> proofFileService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, unknownRoylloCoinDecodedProof));
        assertEquals(e.getMessage(), "Asset " + UNKNOWN_ROYLLO_COIN_ASSET_ID + " is not registered in our database");

        // We add the asset of our proof, and then, our proof --> No error and proof should be added.
        final AssetDTO unknownRoylloCoin = assetService.addAsset(ASSET_MAPPER.mapToAssetDTO(unknownRoylloCoinDecodedProof.getDecodedProof()));
        assertNotNull(unknownRoylloCoin);
        verifyAsset(unknownRoylloCoin, UNKNOWN_ROYLLO_COIN_ASSET_ID);

        // Then, our proof that should be added without any problem.
        final ProofFileDTO proofAdded = proofFileService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, unknownRoylloCoinDecodedProof);
        assertNotNull(proofAdded);
        assertNotNull(proofAdded.getId());
        assertEquals(UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID, proofAdded.getProofFileId());
        assertEquals(UNKNOWN_ROYLLO_COIN_RAW_PROOF, proofAdded.getRawProof());
        assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_ID, proofAdded.getAsset().getAssetId());
        assertEquals(ANONYMOUS_ID, proofAdded.getCreator().getId());

        // We add again our proof as it's already in our database --> an error must occur.
        e = assertThrows(ProofCreationException.class, () -> proofFileService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, unknownRoylloCoinDecodedProof));
        assertEquals(e.getMessage(), "This proof file is already registered with proof id: 882416c100923250bf3d9a6947227c309fdd8f348900b42cd02c5904bfa15f52");
    }

    @Test
    @DisplayName("getProofFilesByAssetId()")
    public void getProofsByAssetId() {
        // =============================================================================================================
        // First case: asset id not found in database.
        AssertionError e = assertThrows(AssertionError.class, () -> proofFileService.getProofFilesByAssetId(ROYLLO_COIN_ASSET_ID, 0, 1));
        assertEquals(e.getMessage(), "Page number starts at page 1");

        // =============================================================================================================
        // Second case: asset id not found in database.
        e = assertThrows(AssertionError.class, () -> proofFileService.getProofFilesByAssetId("NON_EXISTING_ASSET_ID", 1, 1));
        assertEquals(e.getMessage(), "Asset ID not found");

        // =============================================================================================================
        // Getting proofs of "activeRoylloCoin" - One page.
        final Page<ProofFileDTO> activeRoylloCoinProofs = proofFileService.getProofFilesByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID, 1, 10);
        assertEquals(3, activeRoylloCoinProofs.getTotalElements());

        // Proof 1.
        Optional<ProofFileDTO> proof1 = activeRoylloCoinProofs.getContent().stream().filter(proofDTO -> proofDTO.getId() == 10001).findFirst();
        assertTrue(proof1.isPresent());
        assertEquals(ANONYMOUS_ID, proof1.get().getCreator().getId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof1.get().getAsset().getAssetId());
        assertEquals("14e2075827c687217bede3f703cfbc94345717213f4fd34d83b68f8268040691", proof1.get().getProofFileId());
        assertEquals(ACTIVE_ROYLLO_COIN_PROOF_1_RAW_PROOF, proof1.get().getRawProof());

        // Proof 2.
        Optional<ProofFileDTO> proof2 = activeRoylloCoinProofs.getContent().stream().filter(proofDTO -> proofDTO.getId() == 10002).findFirst();
        assertTrue(proof2.isPresent());
        assertEquals(ANONYMOUS_ID, proof2.get().getCreator().getId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof2.get().getAsset().getAssetId());
        assertEquals("23a6c9e1db87a8993490c7578c7ae6d85fee3bc16b9fc7d3c4c756f7452262e1", proof2.get().getProofFileId());
        assertEquals(ACTIVE_ROYLLO_COIN_PROOF_2_RAW_PROOF, proof2.get().getRawProof());

        // Proof 3.
        Optional<ProofFileDTO> proof3 = activeRoylloCoinProofs.getContent().stream().filter(proofDTO -> proofDTO.getId() == 10003).findFirst();
        assertTrue(proof3.isPresent());
        assertEquals(ANONYMOUS_ID, proof3.get().getCreator().getId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof3.get().getAsset().getAssetId());
        assertEquals("e537eddf83dcb34723121860b49579eb4e766ace01bbb81fc7fec233835f2e1e", proof3.get().getProofFileId());
        assertEquals(ACTIVE_ROYLLO_COIN_PROOF_3_RAW_PROOF, proof3.get().getRawProof());

        // =============================================================================================================
        // Getting proofs of "roylloCoin" - One page.
        final Page<ProofFileDTO> roylloCoinProofs = proofFileService.getProofFilesByAssetId(ROYLLO_COIN_ASSET_ID, 1, 10);
        assertEquals(1, roylloCoinProofs.getTotalElements());

        // Proof 1.
        Optional<ProofFileDTO> myRoylloCoinProof1 = roylloCoinProofs.getContent().stream().filter(proofDTO -> proofDTO.getId() == 1).findFirst();
        assertTrue(myRoylloCoinProof1.isPresent());
        assertEquals(ANONYMOUS_ID, myRoylloCoinProof1.get().getCreator().getId());
        assertEquals(ROYLLO_COIN_ASSET_ID, myRoylloCoinProof1.get().getAsset().getAssetId());
        assertEquals(ROYLLO_COIN_PROOF_ID, myRoylloCoinProof1.get().getProofFileId());
        assertEquals(ROYLLO_COIN_RAW_PROOF, myRoylloCoinProof1.get().getRawProof());
    }

    @Test
    @DisplayName("getProofFileByProofFileId()")
    public void getProofByProofId() {
        final Optional<ProofFileDTO> roylloCoinProof = proofFileService.getProofFileByProofFileId("09fcd6349cceea648dc00545846e40b50efdf3c9e27e3d7feb43103f6e593576");

        // Checking proof.
        assertTrue(roylloCoinProof.isPresent());
        assertEquals(ROYLLO_COIN_PROOF_ID, roylloCoinProof.get().getProofFileId());
        assertEquals(ROYLLO_COIN_RAW_PROOF, roylloCoinProof.get().getRawProof());

        // Checking asset.
        assertEquals(1, roylloCoinProof.get().getAsset().getId());
        verifyAsset(roylloCoinProof.get().getAsset(), ROYLLO_COIN_ASSET_ID);
    }

}
