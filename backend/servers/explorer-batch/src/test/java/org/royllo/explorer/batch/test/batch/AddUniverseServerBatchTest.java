package org.royllo.explorer.batch.test.batch;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.AddUniverseServerBatch;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DisplayName("Add universe server batch test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"mempoolTransactionServiceMock", "tapdProofServiceMock", "scheduler-disabled"})
public class AddUniverseServerBatchTest {

    @Autowired
    RequestService requestService;

    @Autowired
    AddUniverseServerBatch addUniverseServerBatch;

    @Test
    @Disabled
    @DisplayName("Add universe server request processing")
    public void batch() {

        // =============================================================================================================
        // We create several requests to be processed by the batch.
        // - 1.1.1.1: Server is responding.
        // - 1.1.1.2: Error code.
        // - 1.1.1.3: Exception.
        final long request1ID = requestService.createAddUniverseServerRequest("1.1.1.1:8080").getId();
        final long request2ID = requestService.createAddUniverseServerRequest("1.1.1.2:8080").getId();
        final long request3ID = requestService.createAddUniverseServerRequest("1.1.1.3:8080").getId();

        // =============================================================================================================
        // We process the requests.
        addUniverseServerBatch.processRequests();

        // =============================================================================================================
        // Testing the results.

        // - 1.1.1.1: Server is responding.
        final Optional<RequestDTO> request1 = requestService.getRequest(request1ID);
        assertTrue(request1.isPresent());
        assertEquals(SUCCESS, request1.get().getStatus());

        // - 1.1.1.2: Error code.
        final Optional<RequestDTO> request2 = requestService.getRequest(request2ID);
        assertTrue(request2.isPresent());
        assertEquals(FAILURE, request1.get().getStatus());

        // - 1.1.1.3: Exception.
        final Optional<RequestDTO> request3 = requestService.getRequest(request3ID);
        assertTrue(request3.isPresent());
        assertEquals(FAILURE, request1.get().getStatus());

    }

}
