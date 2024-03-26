package org.royllo.explorer.core.test.core.service.proof;

import jakarta.validation.ConstraintViolationException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_HOST;
import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_PORT;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.enums.ProofType.PROOF_TYPE_UNSPECIFIED;
import static org.royllo.explorer.core.util.validator.PageNumberValidator.FIRST_PAGE_NUMBER;
import static org.royllo.explorer.core.util.validator.PageSizeValidator.MAXIMUM_PAGE_SIZE;
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
        // Retrieved data from TAPD and check if the data is not already in our database.
        final String UNKNOWN_ROYLLO_COIN_RAW_PROOF = UNKNOWN_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String UNKNOWN_ROYLLO_COIN_PROOF_ID = sha256(UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        DecodedProofResponse unknownRoylloCoinDecodedProof = TAPDService.decode(UNKNOWN_ROYLLO_COIN_RAW_PROOF).block();
        assertNotNull(unknownRoylloCoinDecodedProof);
        assertFalse(assetService.getAssetByAssetIdOrAlias(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());

        // =============================================================================================================
        // Constraint tests.

        // We add our proof but our an asset doesn't exist yet --> an error must occur.

        assertThatExceptionOfType(ProofCreationException.class)
                .isThrownBy(() -> proofService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, PROOF_TYPE_UNSPECIFIED, unknownRoylloCoinDecodedProof))
                .withMessage("Asset " + UNKNOWN_ROYLLO_COIN_ASSET_ID + " is not registered in our database");

        // =============================================================================================================
        // Normal behavior tests.

        // We add the asset linked to our proof, and then, we will add our proof --> No error and proof should be added.
        final AssetDTO unknownRoylloCoin = assetService.addAsset(ASSET_MAPPER.mapToAssetDTO(unknownRoylloCoinDecodedProof.getDecodedProof()));
        assertNotNull(unknownRoylloCoin);
        verifyAsset(unknownRoylloCoin, UNKNOWN_ROYLLO_COIN_ASSET_ID);

        // Then, we add our proof.
        assertThat(proofService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, PROOF_TYPE_UNSPECIFIED, unknownRoylloCoinDecodedProof))
                .isNotNull()
                .satisfies(proofDTO -> {
                    assertNotNull(proofDTO.getId());
                    assertEquals(UNKNOWN_ROYLLO_COIN_PROOF_ID, proofDTO.getProofId());
                    assertEquals(UNKNOWN_ROYLLO_COIN_RAW_PROOF, getProofFromContentService(proofDTO.getProofFileName()));
                    assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_ID, proofDTO.getAsset().getAssetId());
                    assertEquals(ANONYMOUS_ID, proofDTO.getCreator().getId());
                    assertEquals(PROOF_TYPE_UNSPECIFIED, proofDTO.getType());
                });

        // We add again our proof as it's already in our database --> an error must occur.
        assertThatExceptionOfType(ProofCreationException.class)
                .isThrownBy(() -> proofService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, PROOF_TYPE_UNSPECIFIED, unknownRoylloCoinDecodedProof))
                .withMessage("This proof is already registered with proof id: " + UNKNOWN_ROYLLO_COIN_PROOF_ID);
    }

    @Test
    @DisplayName("getProofsByAssetId()")
    public void getProofsByAssetId() {
        // =============================================================================================================
        // Constraint tests.

        // Wrong page number and page size.
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> proofService.getProofsByAssetId(ROYLLO_COIN_ASSET_ID, FIRST_PAGE_NUMBER - 1, MAXIMUM_PAGE_SIZE + 1))
                .satisfies(violations -> {
                    assertEquals(2, violations.getConstraintViolations().size());
                    assertTrue(isPropertyInConstraintViolations(violations, "pageNumber"));
                    assertTrue(isPropertyInConstraintViolations(violations, "pageSize"));
                });

        // Asset null or asset id not found in database.
        assertTrue(proofService.getProofsByAssetId(null, 1, 1).isEmpty());
        assertTrue(proofService.getProofsByAssetId("NON_EXISTING_ASSET_ID", 1, 1).isEmpty());

        // =============================================================================================================
        // Normal behavior tests.

        // Getting proofs of "trickyRoylloCoin".
        final Page<ProofDTO> trickyRoylloCoinProofs = proofService.getProofsByAssetId(TRICKY_ROYLLO_COIN_ASSET_ID, 1, 10);
        assertEquals(3, trickyRoylloCoinProofs.getTotalElements());

        // Proof 1.
        ProofDTO proof1 = trickyRoylloCoinProofs.getContent().stream()
                .filter(proofDTO -> proofDTO.getId() == 6)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Proof 1 not found"));
        assertEquals(ANONYMOUS_ID, proof1.getCreator().getId());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, proof1.getAsset().getAssetId());
        assertEquals(sha256(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof()), proof1.getProofId());
        assertEquals(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof(), getProofFromContentService(proof1.getProofFileName()));

        // Proof 2.
        ProofDTO proof2 = trickyRoylloCoinProofs.getContent().stream()
                .filter(proofDTO -> proofDTO.getId() == 7)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Proof 2 not found"));
        assertEquals(ANONYMOUS_ID, proof2.getCreator().getId());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, proof2.getAsset().getAssetId());
        assertEquals(sha256(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(2).getRawProof()), proof2.getProofId());
        assertEquals(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(2).getRawProof(), getProofFromContentService(proof2.getProofFileName()));

        // Proof 3.
        ProofDTO proof3 = trickyRoylloCoinProofs.getContent().stream()
                .filter(proofDTO -> proofDTO.getId() == 8)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Proof 3 not found"));
        assertEquals(ANONYMOUS_ID, proof3.getCreator().getId());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, proof3.getAsset().getAssetId());
        assertEquals(sha256(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(4).getRawProof()), proof3.getProofId());
        assertEquals(TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(4).getRawProof(), getProofFromContentService(proof3.getProofFileName()));
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
        assertEquals(ROYLLO_COIN_RAW_PROOF, getProofFromContentService(roylloCoinProof.get().getProofFileName()));

        // Checking asset.
        assertEquals(1, roylloCoinProof.get().getAsset().getId());
        verifyAsset(roylloCoinProof.get().getAsset(), ROYLLO_COIN_ASSET_ID);
    }

    /**
     * Returns the proof content from the content service.
     *
     * @param filename filename of the proof
     * @return proof
     */
    private String getProofFromContentService(final String filename) {
        var client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + filename)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }
        return "";
    }

}
