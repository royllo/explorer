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
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;
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
        // TODO Review this test

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

    @Test
    @DisplayName("Universe servers order")
    public void universeServersOrder() throws IOException {
        // TODO Review this test
        // We remove all universe servers.
        universeServerRepository.deleteAll();

        // We create 6 servers :
        // Server 1 : lastSynchronizedOn = now - 1 day.
        universeServerRepository.save(UniverseServer.builder()
                .universeServerId("server1ID")
                .owner(ANONYMOUS_USER)
                .serverAddress("server1")
                .lastSynchronizedOn(ZonedDateTime.now().minus(1, DAYS))
                .build());
        // Server 2 : lastSynchronizedOn = now - 3 days.
        universeServerRepository.save(UniverseServer.builder()
                .universeServerId("server2ID")
                .owner(ANONYMOUS_USER)
                .serverAddress("server2").lastSynchronizedOn(ZonedDateTime.now().minus(3, DAYS))
                .build());
        // server 3 : lastSynchronizedOn = now - 1 month.
        universeServerRepository.save(UniverseServer.builder()
                .universeServerId("server3ID")
                .owner(ANONYMOUS_USER)
                .serverAddress("server3").lastSynchronizedOn(ZonedDateTime.now().minus(1, MONTHS))
                .build());
        // server 4 : lastSynchronizedOn = null.
        universeServerRepository.save(UniverseServer.builder()
                .universeServerId("server4ID")
                .owner(ANONYMOUS_USER)
                .serverAddress("server4").lastSynchronizedOn(null)
                .build());
        // Server 5 : lastSynchronizedOn = now - 2 days.
        universeServerRepository.save(UniverseServer.builder()
                .universeServerId("server5ID")
                .owner(ANONYMOUS_USER)
                .serverAddress("server5").lastSynchronizedOn(ZonedDateTime.now().minus(2, DAYS))
                .build());
        // Server 6 : lastSynchronizedOn = null.
        universeServerRepository.save(UniverseServer.builder()
                .universeServerId("server6ID")
                .owner(ANONYMOUS_USER)
                .serverAddress("server6").lastSynchronizedOn(null)
                .build());


        // We process the universe servers - only 5 at a time, so we run it two times, so we should have this order:
        // server4, server6, server3, server2, server5, server1.
        universeExplorerBatch.processUniverseServers();
        universeExplorerBatch.processUniverseServers();

        final ZonedDateTime lastSynchronizedOnServer1 = getLastSynchronizeOfServer("server1");
        final ZonedDateTime lastSynchronizedOnServer2 = getLastSynchronizeOfServer("server2");
        final ZonedDateTime lastSynchronizedOnServer3 = getLastSynchronizeOfServer("server3");
        final ZonedDateTime lastSynchronizedOnServer4 = getLastSynchronizeOfServer("server4");
        final ZonedDateTime lastSynchronizedOnServer5 = getLastSynchronizeOfServer("server5");
        final ZonedDateTime lastSynchronizedOnServer6 = getLastSynchronizeOfServer("server6");

        assertTrue(lastSynchronizedOnServer4.isBefore(lastSynchronizedOnServer6));
        assertTrue(lastSynchronizedOnServer6.isBefore(lastSynchronizedOnServer3));
        assertTrue(lastSynchronizedOnServer3.isBefore(lastSynchronizedOnServer2));
        assertTrue(lastSynchronizedOnServer2.isBefore(lastSynchronizedOnServer5));
        assertTrue(lastSynchronizedOnServer5.isBefore(lastSynchronizedOnServer1));
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

    /**
     * Get the last synchronize date of a server.
     *
     * @param serverAddress server address
     * @return the last synchronize date of a server.
     */
    private ZonedDateTime getLastSynchronizeOfServer(final String serverAddress) {
        final Optional<UniverseServer> server = universeServerRepository.findByServerAddress(serverAddress);
        return server.map(UniverseServer::getLastSynchronizedOn).orElse(null);
    }

}
