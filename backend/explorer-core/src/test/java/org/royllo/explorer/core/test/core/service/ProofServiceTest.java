package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.service.proof.ProofService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DisplayName("ProofService tests")
public class ProofServiceTest extends BaseTest {

    @Autowired
    private ProofService proofService;

    @Test
    @DisplayName("createProof()")
    public void createProof() {
        // We try to create a proof with an already existing proof (an ID is set!)
        try {
            proofService.createProof(ProofDTO.builder()
                    .id(1L)
                    .rawProof(ROYLLO_COIN_RAW_PROOF)
                    .proofIndex(ROYLLO_COIN_PROOF_INDEX)
                    .build());
            fail("An error should occurred because we have an id for that proof");
        } catch (AssertionError error) {
            assertEquals("Your proof already has an ID, this method can only be used to create a proof" , error.getMessage());
        }

        // We create a proof with correct value.
        ProofDTO createdProof = proofService.createProof(ProofDTO.builder()
                .rawProof(ROYLLO_COIN_RAW_PROOF)
                .proofIndex(ROYLLO_COIN_PROOF_INDEX)
                .build());
        assertNotNull(createdProof);
        assertNotNull(createdProof.getId());
        assertNotNull(createdProof.getProofId());
        assertEquals(ROYLLO_COIN_PROOF_ID, createdProof.getProofId());
        assertEquals(ROYLLO_COIN_RAW_PROOF, createdProof.getRawProof());
        assertEquals(ROYLLO_COIN_PROOF_INDEX, createdProof.getProofIndex());
    }

}
