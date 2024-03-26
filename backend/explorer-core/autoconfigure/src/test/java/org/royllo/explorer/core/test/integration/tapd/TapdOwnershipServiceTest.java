package org.royllo.explorer.core.test.integration.tapd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.test.TapdData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void verifyOwnership() {
        // We retrieve the proof that works for the test - ownershipTest1.
        final var validRequest = TapdData.OWNERSHIP_VERIFY_REQUESTS.entrySet()
                .stream()
                .filter(value -> value.getKey().getProofWithWitness().startsWith("544150500004"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Proof not found not found"));

        // We verify the proof - valid proof result must be set to true.
        assertThat(tapdService.verifyOwnership(validRequest.getKey().getProofWithWitness()).block())
                .isNotNull()
                .satisfies(result -> {
                    assertNull(result.getErrorCode());
                    assertNull(result.getErrorMessage());
                    assertTrue(result.getValidProof());
                });

        // We will try with the simple royllo coin proof (not ownership proof).
        final String ROYLLO_COIN_RAW_PROOF = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        assertThat(tapdService.verifyOwnership(ROYLLO_COIN_RAW_PROOF).block())
                .isNotNull()
                .satisfies(result -> {
                    assertNotNull(result.getErrorCode());
                    assertNotNull(result.getErrorMessage());
                    assertNull(result.getValidProof());
                });
    }

}
