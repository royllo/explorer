package org.royllo.explorer.batch.test.core.universe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddUniverseServerBatch;
import org.royllo.explorer.core.dto.request.AddUniverseServerRequestDTO;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;

@SpringBootTest
@DirtiesContext
@DisplayName("Add universe server batch test")
@ActiveProfiles({"tapdProofServiceMock", "scheduler-disabled"})
public class AddUniverseServerBatchTest {

    @Autowired
    RequestService requestService;

    @Autowired
    AddUniverseServerBatch addUniverseServerBatch;

    @Test
    @DisplayName("Add universe server request processing")
    public void batch() {
        // =============================================================================================================
        // We create several requests to be processed by the batch.
        // - 1.1.1.1: Server is responding.
        // - 1.1.1.2: Error code.
        // - 1.1.1.3: Exception.
        // - 1.1.1.1: Server is responding but duplicated.
        final long request1ID = requestService.createAddUniverseServerRequest("1.1.1.1:8080").getId();
        final long request2ID = requestService.createAddUniverseServerRequest("1.1.1.2:8080").getId();
        final long request3ID = requestService.createAddUniverseServerRequest("1.1.1.3:8080").getId();
        final long request4ID = requestService.createAddUniverseServerRequest("1.1.1.1:8080").getId();

        // =============================================================================================================
        // We process the requests.
        addUniverseServerBatch.processRequests();

        // =============================================================================================================
        // Testing the results.

        // - 1.1.1.1: Server is responding.
        assertThat(requestService.getRequest(request1ID))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertEquals(SUCCESS, request.getStatus());
                    assertNull(request.getErrorMessage());
                    // We cast to test AddUniverseServerRequestDTO specific fields.
                    final AddUniverseServerRequestDTO requestCasted = (AddUniverseServerRequestDTO) request;
                    assertNotNull(requestCasted.getUniverseServer());
                    assertNotNull(requestCasted.getUniverseServer().getId());
                    assertEquals("1.1.1.1:8080", requestCasted.getUniverseServer().getServerAddress());
                });

        // - 1.1.1.2: Error code.
        assertThat(requestService.getRequest(request2ID))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertEquals(FAILURE, request.getStatus());
                    assertEquals("Error: Universe roots response error code: Mocked error message", request.getErrorMessage());
                    // We cast to test AddUniverseServerRequestDTO specific fields.
                    final AddUniverseServerRequestDTO requestCasted = (AddUniverseServerRequestDTO) request;
                    assertNull(requestCasted.getUniverseServer());
                });

        // - 1.1.1.3: Exception.
        assertThat(requestService.getRequest(request3ID))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertEquals(FAILURE, request.getStatus());
                    assertEquals("Error: Mocked exception", request.getErrorMessage());
                    // We cast to test AddUniverseServerRequestDTO specific fields.
                    final AddUniverseServerRequestDTO requestCasted = (AddUniverseServerRequestDTO) request;
                    assertNull(requestCasted.getUniverseServer());
                });

        // - 1.1.1.1: Server is responding but duplicated.
        assertThat(requestService.getRequest(request4ID))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertEquals(FAILURE, request.getStatus());
                    assertEquals("Error: Universe 1.1.1.1:8080 server already exists", request.getErrorMessage());
                });
    }

}
