package org.royllo.explorer.backend.test.core.batch.update;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.backend.repository.universe.UpdateRepository;
import org.royllo.explorer.backend.batch.UpdateBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.royllo.explorer.backend.util.enums.UpdateRequestStatus.ERROR;
import static org.royllo.explorer.backend.util.enums.UpdateRequestStatus.NEW;
import static org.royllo.explorer.backend.util.enums.UpdateRequestStatus.TRANSACTION_OUTPUT_CHECKED;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

/**
 * {@link UpdateBatch} tests.
 */
@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("mempoolTransactionServiceMock")
@DisplayName("UpdateBatch complete tests")
public class UpdateBatchTest {

    @Autowired
    private UpdateBatch updateBatch;

    @Autowired
    private UpdateRepository updateRepository;

    @Test
    @DisplayName("Complete test")
    public void completeTest() {
        // =============================================================================================================
        // Initial values.

        // Updates with "NEW" status.
        assertEquals(3, updateRepository.findByStatus(NEW).size());
        // Updates with "TRANSACTION_OUTPUT_CHECKED" status
        assertEquals(0, updateRepository.findByStatus(TRANSACTION_OUTPUT_CHECKED).size());
        // Updates with "ERROR" status
        assertEquals(0, updateRepository.findByStatus(ERROR).size());

        // =============================================================================================================
        // Running the batch one time.
        updateBatch.run();

        // Updates with "NEW" status.
        assertEquals(0, updateRepository.findByStatus(NEW).size());
        // Updates with "TRANSACTION_OUTPUT_CHECKED" status
        assertEquals(1, updateRepository.findByStatus(TRANSACTION_OUTPUT_CHECKED).size());
        // Updates with "ERROR" status
        assertEquals(2, updateRepository.findByStatus(ERROR).size());
    }

}
