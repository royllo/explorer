package org.royllo.explorer.batch.test.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.PurgeBatch;
import org.royllo.explorer.batch.test.util.BaseTest;
import org.royllo.explorer.core.domain.request.AddProof;
import org.royllo.explorer.core.domain.request.AddUniverseServerRequest;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DisplayName("Purge batch test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"mempoolTransactionServiceMock", "tapdProofServiceMock", "scheduler-disabled"})
public class PurgeBatchTest extends BaseTest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PurgeBatch purgeBatch;

    @Test
    @DisplayName("Purge failed requests")
    public void purgeFailedRequests() {
        // We start by erasing everything.
        requestRepository.deleteAll();

        // =============================================================================================================
        // Requests with "OPENED" status.
        // We create 10 000 "Add proof request" with "OPENED" status.
        for (int i = 1; i <= 10_000; i++) {
            AddProof request = new AddProof();
            request.setRequestId("ID_" + i);
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(OPENED);
            request.setRawProof("Add proof request n°" + i);
            requestRepository.save(request);
        }
        // We should have 10 000 requests.
        assertTrue(requestRepository.findByRequestId("ID_1").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_10000").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_10001").isEmpty());
        assertEquals(10_000, requestRepository.count());

        // We create 10 000 "Add universe server request" with "OPENED" status.
        for (int i = 10_001; i <= 20_000; i++) {
            AddUniverseServerRequest request = new AddUniverseServerRequest();
            request.setRequestId("ID_" + i);
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(OPENED);
            request.setServerAddress("Add proof request n°" + i);
            requestRepository.save(request);
        }
        // We should have 20 000 requests.
        assertTrue(requestRepository.findByRequestId("ID_10001").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_20000").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_20001").isEmpty());
        assertEquals(20_000, requestRepository.count());

        // =============================================================================================================
        // Requests with "FAILURE" status.
        // We create 10 000 "Add proof request" with "FAILURE" status.
        for (int i = 20_001; i <= 30_000; i++) {
            AddProof request = new AddProof();
            request.setRequestId("ID_" + i);
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(FAILURE);
            request.setRawProof("Add proof request n°" + i);
            requestRepository.save(request);
        }
        // We should have 30 000 requests.
        assertTrue(requestRepository.findByRequestId("ID_20001").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_30000").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_30001").isEmpty());
        assertEquals(30_000, requestRepository.count());

        // We create 10 000 "Add universe server request" with "FAILURE" status.
        for (int i = 30_001; i <= 40_000; i++) {
            AddUniverseServerRequest request = new AddUniverseServerRequest();
            request.setRequestId("ID_" + i);
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(FAILURE);
            request.setServerAddress("Add proof request n°" + i);
            requestRepository.save(request);
        }
        // We should have 20 000 requests.
        assertTrue(requestRepository.findByRequestId("ID_30001").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_40000").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_40001").isEmpty());
        assertEquals(40_000, requestRepository.count());

        // =============================================================================================================
        // Requests with "SUCCESS" status.
        // We create 10 000 "Add proof request" with "SUCCESS" status.
        for (int i = 40_001; i <= 50_000; i++) {
            AddProof request = new AddProof();
            request.setRequestId("ID_" + i);
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(SUCCESS);
            request.setRawProof("Add proof request n°" + i);
            requestRepository.save(request);
        }
        // We should have 30 000 requests.
        assertTrue(requestRepository.findByRequestId("ID_40001").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_50000").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_50001").isEmpty());
        assertEquals(50_000, requestRepository.count());

        // We create 10 000 "Add universe server request" with "SUCCESS" status.
        for (int i = 50_001; i <= 60_000; i++) {
            AddUniverseServerRequest request = new AddUniverseServerRequest();
            request.setRequestId("ID_" + i);
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(SUCCESS);
            request.setServerAddress("Add proof request n°" + i);
            requestRepository.save(request);
        }
        // We should have 20 000 requests.
        assertTrue(requestRepository.findByRequestId("ID_50001").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_60000").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_60001").isEmpty());
        assertEquals(60_000, requestRepository.count());

        // =============================================================================================================
        // The rule is that all failed requests besides MAXIMUM_FAILED_REQUESTS_STORE (10 000) must be deleted.
        int totalRequests = requestRepository.findAll().size();
        assertEquals(60_000, totalRequests);
        int failedRequests = requestRepository.findByStatusInOrderById(Collections.singletonList(FAILURE)).size();
        assertEquals(20_000, failedRequests);
        assertTrue(requestRepository.findByRequestId("ID_40001").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_60000").isPresent());

        // =============================================================================================================
        // Running the purge.
        // We have 60 000 requests in total.
        // We have 20 000 requests with "FAILURE" status.
        // 10 000 can only be kept.
        // 10 000 should be deleted, the ones between after 50 000... up to 60 000.
        purgeBatch.purge();

        totalRequests = requestRepository.findAll().size();
        assertEquals(50_000, totalRequests);
        failedRequests = requestRepository.findByStatusInOrderById(Collections.singletonList(FAILURE)).size();
        assertEquals(10_000, failedRequests);
        assertTrue(requestRepository.findByRequestId("ID_20001").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_30000").isPresent());
        assertTrue(requestRepository.findByRequestId("ID_30001").isEmpty());
        assertTrue(requestRepository.findByRequestId("ID_40000").isEmpty());
        assertTrue(requestRepository.findByRequestId("ID_40001").isPresent());
    }

}
