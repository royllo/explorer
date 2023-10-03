package org.royllo.explorer.core.test.integration.tapd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.provider.tapd.UniverseLeavesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"tapd.api.base-url=https://157.230.85.88:8089"})
@DisplayName("TAPD Universe leaves service test")
public class TapdUniverseLeavesServiceTest {

    @Autowired
    private TapdService tapdService;

    @Test
    @DisplayName("Calling getUniverseLeaves() on TAPD")
    public void getUniverseLeavesTest() {
        UniverseLeavesResponse response = tapdService.getUniverseLeaves("https://testnet.universe.lightning.finance",
                "f84238ffd7838b663f1800d8147c9338f15688b430f6e9d8d53f148049ef3bcb").block();

        // Testing the response.
        assertNotNull(response);
        assertEquals(1, response.getLeaves().size());
        assertNotNull(response.getLeaves().get(0));
        assertNotNull(response.getLeaves().get(0).getIssuanceProof());
    }

    @Test
    @DisplayName("Calling getUniverseLeaves() with wrong value on TAPD")
    public void getUniverseLeavesWithWrongValueTest() {
        UniverseLeavesResponse response = tapdService.getUniverseLeaves("https://testnet.universe.lightning.finance",
                "not-existing").block();

        // Testing the response (error).
        assertNotNull(response);
        assertEquals(2, response.getErrorCode());
        assertEquals("encoding/hex: invalid byte: U+006E 'n'", response.getErrorMessage());
    }

}
