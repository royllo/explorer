package org.royllo.explorer.core.test.integration.tapd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.provider.tapd.UniverseRootsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayName("TAPD Universe roots service test")
public class TapdUniverseRootsServiceTest {

    @Autowired
    private TapdService tapdService;

    @Test
    @DisplayName("Calling getUniverseRoots() on TAPD")
    public void getUniverseRootsTest() {
        // TODO Review this test
        UniverseRootsResponse response = tapdService.getUniverseRoots("https://testnet.universe.lightning.finance/v1/taproot-assets/").block();

        // Testing all the value from the response.
        assertNotNull(response);
        assertTrue(response.getUniverseRoots()
                .values()
                .stream()
                .anyMatch(universeRoot -> "0a7d8e5b8e836f69b8210200fdfd4b6c06c7170e91bd45fe365f1e5a2be0e193".equals(universeRoot.getId().getAssetId())));
    }

}
