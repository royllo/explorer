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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
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
        RequestDTO request1Result = requestProcessorService.processRequest(request1.get());
        assertNotNull(request1Result);
        assertEquals(FAILURE, request1Result.getStatus());
        assertTrue(request1Result.getErrorMessage().startsWith("Transaction not found ("));
    }

}
