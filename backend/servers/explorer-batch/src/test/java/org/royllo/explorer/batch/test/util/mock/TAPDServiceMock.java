package org.royllo.explorer.batch.test.util.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.provider.tapd.UniverseLeavesResponse;
import org.royllo.explorer.core.provider.tapd.UniverseRootsResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Profile("tapdProofServiceMock")
@Configuration
public class TAPDServiceMock {

    @Bean
    @Primary
    public TapdService tapdProofService() throws IOException {
        final TapdService mockedService = Mockito.mock(TapdService.class);

        // =============================================================================================================
        // Mocks for AddUniverseServerBatchTest.

        // - 1.1.1.1: Server is responding.
        UniverseRootsResponse universeRootsResponse = new UniverseRootsResponse();
        UniverseRootsResponse.UniverseRoot universeRoot = new UniverseRootsResponse.UniverseRoot();
        UniverseRootsResponse.ID id = new UniverseRootsResponse.ID();
        id.setAssetId("asset1");
        universeRoot.setId(id);
        Map<String, UniverseRootsResponse.UniverseRoot> map = new HashMap<>();
        map.put("asset1", universeRoot);
        universeRootsResponse.setUniverseRoots(map);
        Mockito.when(mockedService.getUniverseRoots("1.1.1.1:8080", 0, 1)).thenReturn(Mono.just(universeRootsResponse));

        // - 1.1.1.2: Error code.
        UniverseRootsResponse universeRootsResponse2 = new UniverseRootsResponse();
        universeRootsResponse2.setErrorCode(1L);
        universeRootsResponse2.setErrorMessage("Mocked error message");
        Mockito.when(mockedService.getUniverseRoots("1.1.1.2:8080", 0, 1)).thenReturn(Mono.just(universeRootsResponse2));

        // - 1.1.1.3: Exception.
        Mockito.when(mockedService.getUniverseRoots("1.1.1.3:8080", 0, 1)).thenThrow(new RuntimeException("Mocked exception"));

        // =============================================================================================================
        // Mocks for UniverseExplorerBatchTest.

        // testnet.universe.lightning.finance (roots).
        Mockito.when(mockedService.getUniverseRoots("testnet.universe.lightning.finance", 0, 100))
                .thenReturn(Mono.just(getUniverseRootsResponse("tapd/universe-roots-response-for-testnet-universe-lightning-finance.json")));
        // testnet.universe.lightning.finance:asset_id_1.
        Mockito.when(mockedService.getUniverseLeaves("testnet.universe.lightning.finance", "asset_id_1"))
                .thenReturn(Mono.just(getUniverseLeavesResponse("tapd/universe-leaves-asset-id-1-for-testnet-universe-lightning-finance.json")));
        // testnet.universe.lightning.finance:asset_id_2.
        Mockito.when(mockedService.getUniverseLeaves("testnet.universe.lightning.finance", "asset_id_2"))
                .thenReturn(Mono.just(getUniverseLeavesResponse("tapd/universe-leaves-asset-id-2-for-testnet-universe-lightning-finance.json")));
        // testnet.universe.lightning.finance:asset_id_3.
        Mockito.when(mockedService.getUniverseLeaves("testnet.universe.lightning.finance", "asset_id_3"))
                .thenReturn(Mono.just(getUniverseLeavesResponse("tapd/universe-leaves-asset-id-3-for-testnet-universe-lightning-finance.json")));

        // testnet2.universe.lightning.finance (roots).
        Mockito.when(mockedService.getUniverseRoots("testnet2.universe.lightning.finance", 0, 100))
                .thenReturn(Mono.just(getUniverseRootsResponse("tapd/universe-roots-response-for-testnet2-universe-lightning-finance.json")));
        // testnet2.universe.lightning.finance:asset_id_1.
        Mockito.when(mockedService.getUniverseLeaves("testnet2.universe.lightning.finance", "asset_id_1"))
                .thenReturn(Mono.just(getUniverseLeavesResponse("tapd/universe-leaves-asset-id-1-for-testnet2-universe-lightning-finance.json")));
        // testnet2.universe.lightning.finance:asset_id_4.
        Mockito.when(mockedService.getUniverseLeaves("testnet2.universe.lightning.finance", "asset_id_4"))
                .thenReturn(Mono.just(getUniverseLeavesResponse("tapd/universe-leaves-asset-id-4-for-testnet2-universe-lightning-finance.json")));
        // testnet2.universe.lightning.finance:asset_id_5.
        Mockito.when(mockedService.getUniverseLeaves("testnet2.universe.lightning.finance", "asset_id_5"))
                .thenReturn(Mono.just(getUniverseLeavesResponse("tapd/universe-leaves-asset-id-5-for-testnet2-universe-lightning-finance.json")));

        return mockedService;
    }

    /**
     * Returns a UniverseRootsResponse loaded from a file.
     *
     * @param filePath file path
     * @return UniverseRootsResponse
     * @throws IOException file not found
     */
    private UniverseRootsResponse getUniverseRootsResponse(final String filePath) throws IOException {
        final ClassPathResource classPathResource = new ClassPathResource(filePath);
        return new ObjectMapper().readValue(classPathResource.getInputStream(), UniverseRootsResponse.class);
    }

    /**
     * Returns a UniverseLeavesResponse loaded from a file.
     *
     * @param filePath file path
     * @return UniverseLeavesResponse
     * @throws IOException file not found
     */
    private UniverseLeavesResponse getUniverseLeavesResponse(final String filePath) throws IOException {
        final ClassPathResource classPathResource = new ClassPathResource(filePath);
        return new ObjectMapper().readValue(classPathResource.getInputStream(), UniverseLeavesResponse.class);
    }

}
