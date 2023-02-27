package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.proof.ProofService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DisplayName("ProofService tests")
public class ProofServiceTest extends BaseTest {

    @Autowired
    private ProofService proofService;

    @Autowired
    private AssetService assetService;

    @Test
    @DisplayName("addProof()")
    public void addProof() {
        // We add our proof for an asset that doesn't exist --> an error must occur.

        // We add the asset of our proof, and then, our proof --> No error, proof should be added.

        // We add again our proof as it's already in our database --> an error must occur.

        /*
 If the proof is already in the database, we should have an error.
        try {
            proofService.addProof(ProofDTO.builder()
                    .id(1L)
                    .rawProof(ROYLLO_COIN_RAW_PROOF)
                    .proofIndex(ROYLLO_COIN_PROOF_INDEX)
                    .build());
            fail("An error should occur because we have an id for that proof");
        } catch (AssertionError error) {
            assertEquals("Your proof already has an ID, this method can only be used to create a proof" , error.getMessage());
        }
 We create a proof with correct value.
        ProofDTO createdProof = proofService.addProof(ProofDTO.builder()
                .rawProof(ROYLLO_COIN_RAW_PROOF)
                .proofIndex(ROYLLO_COIN_PROOF_INDEX)
                .build());
        assertNotNull(createdProof);
        assertNotNull(createdProof.getId());
        assertNotNull(createdProof.getProofId());
        assertEquals(ROYLLO_COIN_PROOF_ID, createdProof.getProofId());
        assertEquals(ROYLLO_COIN_RAW_PROOF, createdProof.getRawProof());
        assertEquals(ROYLLO_COIN_PROOF_INDEX, createdProof.getProofIndex());
*/
    }

}
