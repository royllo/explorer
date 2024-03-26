package org.royllo.explorer.core.test.integration.tapd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.provider.tapd.UniverseRootsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"tapd.api.base-url=https://157.230.85.88:8089"})
@DirtiesContext
@DisplayName("TAPD Universe roots service test")
public class TapdUniverseRootsServiceTest {

    @Autowired
    private TapdService tapdService;

    @Test
    @DisplayName("getUniverseRoots()")
    public void getUniverseRoots() throws InterruptedException, UnknownHostException {
        // List of servers to test.
        String[] validServers = new String[]{
                // Without "https".
                "testnet.universe.lightning.finance",
                "testnet.universe.lightning.finance/",
                InetAddress.getByName("testnet.universe.lightning.finance").getHostAddress(),
                // With "https".
                "https://testnet.universe.lightning.finance",
                "https://testnet.universe.lightning.finance/",
        };

        // Testing each server.
        for (String serverAddress : validServers) {
            // Testing the response of each server.
            UniverseRootsResponse response = tapdService.getUniverseRoots(serverAddress, 0, 100).block();
            assertNotNull(response);
            assertThat(response.getUniverseRoots().values())
                    .extracting(universeRoot -> universeRoot.getId().getAssetId())
                    .contains("05c34a505589025a0a78c31237e560406e4a2c5dc5a41c4ece6f96abbe77ad53");

            // Let the distant server rest for sometime.
            TimeUnit.SECONDS.sleep(1);
        }
    }

}
