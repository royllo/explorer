package org.royllo.explorer.batch.test.core.universe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.universe.UniverseExplorerBatch;
import org.royllo.explorer.core.domain.request.AddProofRequest;
import org.royllo.explorer.core.domain.universe.UniverseServer;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.repository.universe.UniverseServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DisplayName("Universe explorer batch test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles({"tapdProofServiceMock", "scheduler-disabled"})
public class UniverseExplorerBatchTest {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    UniverseServerRepository universeServerRepository;

    @Autowired
    UniverseExplorerBatch universeExplorerBatch;

    @Test
    @DisplayName("Universe explorer batch test")
    public void batch() throws IOException {
        long count = requestRepository.count();
        // We check that the universe servers have never been contacted.
        Optional<UniverseServer> server1 = universeServerRepository.findByServerAddress("testnet.universe.lightning.finance");
        assertTrue(server1.isPresent());
        assertNull(server1.get().getLastSynchronizedOn());
        Optional<UniverseServer> server2 = universeServerRepository.findByServerAddress("testnet2.universe.lightning.finance");
        assertTrue(server2.isPresent());
        assertNull(server2.get().getLastSynchronizedOn());

        // We check that we don't have the request for the proofs find in universe servers
        assertEquals(0, findAddProofRequestByProof("asset_id_1_proof").size());
        assertEquals(0, findAddProofRequestByProof("asset_id_2_proof").size());
        assertEquals(0, findAddProofRequestByProof("asset_id_3_proof").size());
        assertEquals(0, findAddProofRequestByProof("asset_id_4_proof").size());
        assertEquals(0, findAddProofRequestByProof("asset_id_5_proof").size());

        // In database, we have two universe servers.
        // The first server lists three assets : asset_id_1, asset_id_2 and asset_id_3.
        // The second server lists three assets : asset_id_1, asset_id_4, asset_id_5.
        // asset_id_1 is on both servers.
        universeExplorerBatch.processUniverseServers();
        universeExplorerBatch.processUniverseServers();

        // We check that the universe servers have been contacted.
        server1 = universeServerRepository.findByServerAddress("testnet.universe.lightning.finance");
        assertTrue(server1.isPresent());
        assertNotNull(server1.get().getLastSynchronizedOn());
        server2 = universeServerRepository.findByServerAddress("testnet2.universe.lightning.finance");
        assertTrue(server2.isPresent());
        assertNotNull(server2.get().getLastSynchronizedOn());

        // We should have more requests.
        // universe-roots-response-for-testnet-universe-lightning-finance.json:
        // - "asset_id": "asset_id_1"
        // - "asset_id": "asset_id_2"
        // - "asset_id": "asset_id_3"
        // universe-roots-response-for-testnet2-universe-lightning-finance.json:
        // - "asset_id": "asset_id_4"
        // - "asset_id": "asset_id_1
        // - "asset_id": "asset_id_5"
        assertEquals(2, findAddProofRequestByProof("asset_id_1_proof").size());
        assertEquals(1, findAddProofRequestByProof("asset_id_2_proof").size());
        assertEquals(1, findAddProofRequestByProof("asset_id_3_proof").size());
        assertEquals(1, findAddProofRequestByProof("asset_id_4_proof").size());
        assertEquals(1, findAddProofRequestByProof("asset_id_5_proof").size());
        assertEquals(count + 6, requestRepository.count());
    }

    /**
     * Find an add proof request by its proof.
     *
     * @param proof the proof.
     * @return an add proof request.
     */
    private List<AddProofRequest> findAddProofRequestByProof(final String proof) {
        return requestRepository.findByStatusOrderById(OPENED)
                .stream()
                .filter(request -> request instanceof AddProofRequest)
                .map(request -> (AddProofRequest) request)
                .filter(request -> request.getProof() != null)
                .filter(request -> request.getProof().equals(proof))
                .toList();
    }

}
