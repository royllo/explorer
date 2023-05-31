package org.royllo.explorer.batch.test.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.PurgeBatch;
import org.royllo.explorer.batch.test.util.BaseTest;
import org.royllo.explorer.core.domain.request.AddProof;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.RECOVERABLE_FAILURE;
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

        // We create "OPENED" requests.
        for (int i = 1; i < 1_000; i++) {
            AddProof request = new AddProof();
            request.setRequestId("OPENED-" + String.format("%05d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(OPENED);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create "SUCCESS" requests.
        for (int i = 1_000; i < 2_000; i++) {
            AddProof request = new AddProof();
            request.setRequestId("SUCCESS-" + String.format("%05d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(SUCCESS);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create "RECOVERABLE_FAILURE" requests.
        for (int i = 2_000; i < 3_000; i++) {
            AddProof request = new AddProof();
            request.setRequestId("RECOVERABLE_FAILURE-" + String.format("%05d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(RECOVERABLE_FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create "FAILURE" requests.
        for (int i = 3_000; i < 15_000; i++) {
            AddProof request = new AddProof();
            request.setRequestId("FAILURE-" + String.format("%05d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We should have 15 000 requests (all status).
        // And there is 12 000 requests with the "FAILURE" status (15 000 - 3 000).
        assertEquals(14_999, requestRepository.count());
        assertEquals(12_000, requestRepository.findByStatusInOrderById(Collections.singletonList(FAILURE)).size());

        // We make a purge.
        // After the purge, only MAXIMUM_FAILED_REQUESTS_STORE (10 000) should be left.
        // We delete the request found on the second page (with a page size of 10 000).
        purgeBatch.purge();
        assertEquals(10_000, requestRepository.findByStatusInOrderById(Collections.singletonList(FAILURE)).size());
    }

}
