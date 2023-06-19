package org.royllo.explorer.batch.test.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.universe.UniverseExplorerBatch;
import org.royllo.explorer.core.domain.request.AddProofRequest;
import org.royllo.explorer.core.domain.universe.UniverseServer;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.repository.universe.UniverseServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Collections;
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
@ActiveProfiles({"mempoolTransactionServiceMock", "tapdProofServiceMock", "scheduler-disabled"})
public class UniverseExplorerBatchTest {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    UniverseServerRepository universeServerRepository;

    @Autowired
    TapdService tapdService;

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
        assertEquals(0, findAddProofRequestByRawProof("asset_id_1_proof").size());
        assertEquals(0, findAddProofRequestByRawProof("asset_id_2_proof").size());
        assertEquals(0, findAddProofRequestByRawProof("asset_id_3_proof").size());
        assertEquals(0, findAddProofRequestByRawProof("asset_id_4_proof").size());

        // In database, we have two universe servers.
        // The first server lists three assets : asset_id_1, asset_id_2 and asset_id_3
        // The second server lists two assets : asset_id_4, asset_id_1 (already defined by the first server)
        // and asset_id_5 (proof already in database);
        universeExplorerBatch.processUniverseServers();

        // We check that the universe servers have been contacted.
        server1 = universeServerRepository.findByServerAddress("testnet.universe.lightning.finance");
        assertTrue(server1.isPresent());
        assertNotNull(server1.get().getLastSynchronizedOn());
        server2 = universeServerRepository.findByServerAddress("testnet2.universe.lightning.finance");
        assertTrue(server2.isPresent());
        assertNotNull(server2.get().getLastSynchronizedOn());

        // We should have 5 more requests in database.
        assertEquals(count + 5, requestRepository.count());
        assertEquals(2, findAddProofRequestByRawProof("asset_id_1_proof").size());
        assertEquals(1, findAddProofRequestByRawProof("asset_id_2_proof").size());
        assertEquals(1, findAddProofRequestByRawProof("asset_id_3_proof").size());
        assertEquals(1, findAddProofRequestByRawProof("asset_id_4_proof").size());
        // We do not create a request for "asset_id_5_proof" because the proof is already in database.
        assertEquals(0, findAddProofRequestByRawProof("asset_id_5_proof").size());
    }

    /**
     * Find an add proof request by its raw proof.
     *
     * @param rawProof the raw proof.
     * @return an add proof request.
     */
    private List<AddProofRequest> findAddProofRequestByRawProof(final String rawProof) {
        return requestRepository.findByStatusInOrderById(Collections.singletonList(OPENED))
                .stream()
                .filter(request -> request instanceof AddProofRequest)
                .map(request -> (AddProofRequest) request)
                .filter(request -> request.getRawProof().equals(rawProof))
                .toList();
    }

}
