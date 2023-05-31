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

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private DataSource dataSource;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PurgeBatch purgeBatch;

    @Test
    @DisplayName("Purge failed requests")
    public void purgeFailedRequests() throws SQLException {
        // We start by erasing everything.
        requestRepository.deleteAll();

        // Requests used to change the request creation date.
        final String updateDate = "UPDATE REQUESTS SET CREATED_ON = ? WHERE REQUEST_ID like ?;";

        // We create 100 "OPENED" requests created before current date minus one month.
        for (int i = 1; i <= 99; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-BEFORE-OPENED-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(OPENED);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "OPENED" requests created after current date minus one month.
        for (int i = 100; i <= 199; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-AFTER-OPENED-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(OPENED);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create 100 "SUCCESS" requests created before one month ago.
        for (int i = 200; i <= 299; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-BEFORE-SUCCESS-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(SUCCESS);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "SUCCESS" requests created after current date minus one month.
        for (int i = 300; i <= 399; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-AFTER-SUCCESS-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(SUCCESS);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create 100 "RECOVERABLE_FAILURE" requests created before one month ago.
        for (int i = 400; i <= 499; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-BEFORE-RECOVERABLE_FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(RECOVERABLE_FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "RECOVERABLE_FAILURE" requests created after current date minus one month.
        for (int i = 500; i <= 599; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-AFTER-RECOVERABLE_FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(RECOVERABLE_FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create 100 "FAILURE" requests created before one month ago.
        for (int i = 600; i <= 699; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-BEFORE-FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "FAILURE" requests created after current date minus one month.
        for (int i = 700; i <= 799; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-AFTER-FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We should have 799 requests.
        assertEquals(799, requestRepository.count());

        // We update the creation date to make our test.
        PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(updateDate);
        preparedStatement.setTimestamp(1, Timestamp.from(ZonedDateTime.now().minusMonths(3).toInstant()));
        preparedStatement.setString(2, "%-BEFORE-%");
        preparedStatement.execute();
        preparedStatement = dataSource.getConnection().prepareStatement(updateDate);
        preparedStatement.setTimestamp(1, Timestamp.from(ZonedDateTime.now().minusDays(3).toInstant()));
        preparedStatement.setString(2, "%-AFTER-%");
        preparedStatement.execute();

        // We check this that we find 100 requests with status "RECOVERABLE_FAILURE" created before one month ago.
        // And we check that they are the good ones (having "BEFORE-RECOVERABLE_FAILURE-" in their request id).
        assertEquals(200, requestRepository.findByStatusInOrderById(Collections.singletonList(RECOVERABLE_FAILURE)).size());
        assertEquals(100, requestRepository.findByStatusInAndCreatedOnBefore(Collections.singletonList(RECOVERABLE_FAILURE),
                ZonedDateTime.now().minusMonths(1)).size());
        requestRepository.findByStatusInAndCreatedOnBefore(Collections.singletonList(RECOVERABLE_FAILURE),
                        ZonedDateTime.now().minusMonths(1))
                .forEach(request -> assertTrue(request.getRequestId().contains("BEFORE-RECOVERABLE_FAILURE-")));

        // After running the purge, we should not have any request with status "BEFORE-RECOVERABLE_FAILURE".
        // 100 should have disappeared and no more "BEFORE-RECOVERABLE_FAILURE-" in their request id.
        purgeBatch.purge();
        assertEquals(100, requestRepository.findByStatusInOrderById(Collections.singletonList(RECOVERABLE_FAILURE)).size());
        requestRepository.findByStatusInAndCreatedOnBefore(Collections.singletonList(RECOVERABLE_FAILURE),
                        ZonedDateTime.now().minusMonths(1))
                .forEach(request -> assertFalse(request.getRequestId().contains("BEFORE-RECOVERABLE_FAILURE-")));
    }

}
