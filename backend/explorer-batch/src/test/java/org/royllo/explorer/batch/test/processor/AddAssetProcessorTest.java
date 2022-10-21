package org.royllo.explorer.batch.test.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.service.RequestProcessorService;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"mempoolTransactionServiceMock", "scheduler-disabled"})
public class AddAssetProcessorTest {

    @Autowired
    RequestService requestService;

    @Autowired
    RequestProcessorService requestProcessorService;

    @Test
    @DisplayName("Process")
    public void process() {
        // =============================================================================================================
        // Add asset request n°1 - Transaction not found.
        final Optional<RequestDTO> request1 = requestService.getRequest(1L);
        assertTrue(request1.isPresent());
        // Checking the result.
        RequestDTO request1Result = requestProcessorService.processRequest(request1.get());
        assertNotNull(request1Result);
        assertEquals(FAILURE, request1Result.getStatus());
        assertTrue(request1Result.getErrorMessage().startsWith("Transaction not found ("));
        // Checking the data in database.
        final Optional<RequestDTO> request1FromService = requestService.getRequest(1L);
        assertTrue(request1FromService.isPresent());
        assertEquals(FAILURE, request1FromService.get().getStatus());
        assertTrue(request1FromService.get().getErrorMessage().startsWith("Transaction not found ("));

        // =============================================================================================================
        // Add asset request n°1 AGAIN - Request already closed (success).
        final Optional<RequestDTO> request1Bis = requestService.getRequest(1L);
        assertTrue(request1Bis.isPresent());
        try {
            requestProcessorService.processRequest(request1Bis.get());
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("This request has already been treated"));
        }

        // =============================================================================================================
        // Add asset request n°2 - Request already closed (failure).
        final Optional<RequestDTO> request2 = requestService.getRequest(2L);
        assertTrue(request2.isPresent());
        try {
            requestProcessorService.processRequest(request2.get());
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("This request has already been treated"));
        }

        // =============================================================================================================
        // Add asset request n°3 - Request with an existing transaction but an invalid proof.
        final Optional<RequestDTO> request3 = requestService.getRequest(3L);
        assertTrue(request3.isPresent());
        // Checking the result.
        RequestDTO request3Result = requestProcessorService.processRequest(request3.get());
        assertNotNull(request3Result);
        assertEquals(FAILURE, request3Result.getStatus());
        assertTrue(request3Result.getErrorMessage().startsWith("Invalid proof"));
        // Checking the data in database.
        final Optional<RequestDTO> request3FromService = requestService.getRequest(3L);
        assertTrue(request3FromService.isPresent());
        assertEquals(FAILURE, request3FromService.get().getStatus());
        assertTrue(request3FromService.get().getErrorMessage().startsWith("Invalid proof"));

        // =============================================================================================================
        // Add asset request n°4 - Request with an existing transaction and a valid proof.
        final Optional<RequestDTO> request4 = requestService.getRequest(4L);
        assertTrue(request4.isPresent());
        // Checking the result.
        RequestDTO request4Result = requestProcessorService.processRequest(request4.get());
        assertNotNull(request4Result);
        assertEquals(SUCCESS, request4Result.getStatus());
        assertNull(request4Result.getErrorMessage());
        // Checking the data in database.
        final Optional<RequestDTO> request4FromService = requestService.getRequest(4L);
        assertTrue(request4FromService.isPresent());
        assertEquals(SUCCESS, request4Result.getStatus());
        assertNull(request4Result.getErrorMessage());
    }

}
