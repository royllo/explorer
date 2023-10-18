package org.royllo.explorer.core.test.integration.tapd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.provider.tapd.UniverseRootsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"tapd.api.base-url=https://157.230.85.88:8089"})
@DisplayName("Lightning TAPD Universe roots service test")
public class TapdUniverseRootsServiceTest {

    @Autowired
    private TapdService tapdService;

    @Test
    @DisplayName("getUniverseRoots() on lightning TAPD")
    public void getUniverseRootsTest() throws InterruptedException {
        // List of servers to test.
        String[] validServers = new String[]{
                "testnet.universe.lightning.finance",
                "testnet.universe.lightning.finance/",
                "52.88.202.111",
                "52.88.202.111/",
                // With https in front.
                "https://testnet.universe.lightning.finance",
                "https://testnet.universe.lightning.finance/",
                "https://52.88.202.111",
                "https://52.88.202.111/",
        };

        // Testing each server.
        for (String serverAddress : validServers) {
            // Testing the response of each server.
            UniverseRootsResponse response = tapdService.getUniverseRoots(serverAddress).block();
            assertNotNull(response);
            assertTrue(response.getUniverseRoots()
                    .values()
                    .stream()
                    .anyMatch(universeRoot -> "05c34a505589025a0a78c31237e560406e4a2c5dc5a41c4ece6f96abbe77ad53".equals(universeRoot.getId().getAssetId())));

            // Let the distant server rest for sometime.
            TimeUnit.SECONDS.sleep(1);
        }
    }

}
