package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.proof.ProofService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.royllo.explorer.core.util.exceptions.proof.ProofCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.enums.ProofType.PROOF_TYPE_UNSPECIFIED;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_COIN_FROM_TEST;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_FROM_TEST;
import static org.royllo.test.TapdData.UNKNOWN_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.UNKNOWN_ROYLLO_COIN_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("ProofService tests")
public class ProofServiceTest extends TestWithMockServers {

    @Autowired
    private TapdService TAPDService;

    @Autowired
    private ProofService proofService;

    @Autowired
    private AssetService assetService;

    @Test
    @DisplayName("addProof()")
    public void addProof() {
        // Retrieved data from TAPD.
        final String UNKNOWN_ROYLLO_COIN_RAW_PROOF = UNKNOWN_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String UNKNOWN_ROYLLO_COIN_PROOF_ID = sha256(UNKNOWN_ROYLLO_COIN_RAW_PROOF);

        // =============================================================================================================
        // Unknown Royllo coin.
        DecodedProofResponse unknownRoylloCoinDecodedProof = TAPDService.decode(UNKNOWN_ROYLLO_COIN_RAW_PROOF).block();
        assertNotNull(unknownRoylloCoinDecodedProof);

        // We add our proof but our an asset doesn't exist yet --> an error must occur.
        assertFalse(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        ProofCreationException e = assertThrows(ProofCreationException.class, () -> proofService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, PROOF_TYPE_UNSPECIFIED, unknownRoylloCoinDecodedProof));
        assertEquals(e.getMessage(), "Asset " + UNKNOWN_ROYLLO_COIN_ASSET_ID + " is not registered in our database");

        // We add the asset of our proof, and then, our proof --> No error and proof should be added.
        final AssetDTO unknownRoylloCoin = assetService.addAsset(ASSET_MAPPER.mapToAssetDTO(unknownRoylloCoinDecodedProof.getDecodedProof()));
        assertNotNull(unknownRoylloCoin);
        verifyAsset(unknownRoylloCoin, UNKNOWN_ROYLLO_COIN_ASSET_ID);

        // Then, our proof that should be added without any problem.
        final ProofDTO proofAdded = proofService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, PROOF_TYPE_UNSPECIFIED, unknownRoylloCoinDecodedProof);
        assertNotNull(proofAdded.getId());
        assertEquals(UNKNOWN_ROYLLO_COIN_PROOF_ID, proofAdded.getProofId());
        assertEquals(UNKNOWN_ROYLLO_COIN_RAW_PROOF, proofAdded.getProof());
        assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_ID, proofAdded.getAsset().getAssetId());
        assertEquals(ANONYMOUS_ID, proofAdded.getCreator().getId());
        assertEquals(PROOF_TYPE_UNSPECIFIED, proofAdded.getProofType());

        // We add again our proof as it's already in our database --> an error must occur.
        e = assertThrows(ProofCreationException.class, () -> proofService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, PROOF_TYPE_UNSPECIFIED, unknownRoylloCoinDecodedProof));
        assertEquals(e.getMessage(), "This proof is already registered with proof id: " + UNKNOWN_ROYLLO_COIN_PROOF_ID);
    }

    @Test
    @DisplayName("getProofByAssetId()")
    public void getProofByAssetId() {
        // =============================================================================================================
        // First case: asset id not found in database.
        AssertionError e = assertThrows(AssertionError.class, () -> proofService.getProofByAssetId(ROYLLO_COIN_ASSET_ID, 0, 1));
        assertEquals(e.getMessage(), "Page number starts at page 1");

        // =============================================================================================================
        // Second case: asset id not found in database.
        e = assertThrows(AssertionError.class, () -> proofService.getProofByAssetId("NON_EXISTING_ASSET_ID", 1, 1));
        assertEquals(e.getMessage(), "Asset ID not found");

        // =============================================================================================================
        // Getting proofs of "trickyRoylloCoin".
        final Page<ProofDTO> trickyRoylloCoinProofs = proofService.getProofByAssetId(TRICKY_ROYLLO_COIN_ASSET_ID, 1, 10);
        assertEquals(3, trickyRoylloCoinProofs.getTotalElements());

        // Proof 1.
        Optional<ProofDTO> proof1 = trickyRoylloCoinProofs.getContent().stream().filter(proofDTO -> proofDTO.getId() == 6).findFirst();
        assertTrue(proof1.isPresent());
        assertEquals(ANONYMOUS_ID, proof1.get().getCreator().getId());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, proof1.get().getAsset().getAssetId());
        assertEquals(sha256(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof()), proof1.get().getProofId());
        assertEquals(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof(), proof1.get().getProof());

        // Proof 2.
        Optional<ProofDTO> proof2 = trickyRoylloCoinProofs.getContent().stream().filter(proofDTO -> proofDTO.getId() == 7).findFirst();
        assertTrue(proof2.isPresent());
        assertEquals(ANONYMOUS_ID, proof2.get().getCreator().getId());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, proof2.get().getAsset().getAssetId());
        assertEquals(sha256(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(2).getRawProof()), proof2.get().getProofId());
        assertEquals(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(2).getRawProof(), proof2.get().getProof());

        // Proof 3.
        Optional<ProofDTO> proof3 = trickyRoylloCoinProofs.getContent().stream().filter(proofDTO -> proofDTO.getId() == 8).findFirst();
        assertTrue(proof3.isPresent());
        assertEquals(ANONYMOUS_ID, proof3.get().getCreator().getId());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, proof3.get().getAsset().getAssetId());
        assertEquals(sha256(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(4).getRawProof()), proof3.get().getProofId());
        assertEquals(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(4).getRawProof(), proof3.get().getProof());
    }

    @Test
    @DisplayName("getProofByProofId()")
    public void getProofByProofId() {
        // Retrieved data from TAPD.
        final String ROYLLO_COIN_RAW_PROOF = ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String ROYLLO_COIN_PROOF_ID = sha256(ROYLLO_COIN_RAW_PROOF);

        final Optional<ProofDTO> roylloCoinProof = proofService.getProofByProofId(ROYLLO_COIN_PROOF_ID);

        // Checking proof.
        assertTrue(roylloCoinProof.isPresent());
        assertEquals(ROYLLO_COIN_PROOF_ID, roylloCoinProof.get().getProofId());
        assertEquals(ROYLLO_COIN_RAW_PROOF, roylloCoinProof.get().getProof());

        // Checking asset.
        assertEquals(1, roylloCoinProof.get().getAsset().getId());
        verifyAsset(roylloCoinProof.get().getAsset(), ROYLLO_COIN_ASSET_ID);
    }

}
