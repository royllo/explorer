package org.royllo.explorer.core.test.integration.tapd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.test.TapdData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_FROM_TEST;

@SpringBootTest(properties = {"tapd.api.base-url=https://testnet.universe.royllo.org:8089"})
@DirtiesContext
@DisplayName("TAPD ownership service test")
public class TapdOwnershipServiceTest {

    @Autowired
    private TapdService tapdService;

    @Test
    @DisplayName("verifyOwnership()")
    public void ownershipVerify() {

        // We retrieve the proof that works for the test - ownershipTest1.
        final var validRequest = TapdData.OWNERSHIP_VERIFY_REQUESTS.entrySet()
                .stream()
                .filter(value -> value.getKey().getProofWithWitness().startsWith("544150500004"))
                .findFirst();
        assertTrue(validRequest.isPresent());

        // We verify the proof.
        var result = tapdService.verifyOwnership(validRequest.get().getKey().getProofWithWitness()).block();
        assertNotNull(result);
        assertNull(result.getErrorCode());
        assertNull(result.getErrorMessage());
        assertTrue(result.getValidProof());

        // We will try with the proof (not ownership proof) of roylloCoin (in mainnnet).
        final String ROYLLO_COIN_RAW_PROOF = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        result = tapdService.verifyOwnership(ROYLLO_COIN_RAW_PROOF).block();
        assertNotNull(result);
        assertNotNull(result.getErrorCode());
        assertNotNull(result.getErrorMessage());
        assertNull(result.getValidProof());
    }

}
