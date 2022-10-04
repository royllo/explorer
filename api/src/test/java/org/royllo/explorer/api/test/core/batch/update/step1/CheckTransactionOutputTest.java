package org.royllo.explorer.api.test.core.batch.update.step1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.domain.universe.Update;
import org.royllo.explorer.api.repository.universe.UpdateRepository;
import org.royllo.explorer.api.service.universe.UpdateService;
import org.royllo.explorer.api.batch.UpdateBatch;
import org.royllo.explorer.api.dto.universe.UpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.api.util.enums.UpdateRequestStatus.ERROR;
import static org.royllo.explorer.api.util.enums.UpdateRequestStatus.TRANSACTION_OUTPUT_CHECKED;
import static org.royllo.explorer.api.util.enums.UserRole.USER;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

/**
 * {@link UpdateBatch} tests.
 */
@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("mempoolTransactionServiceMock")
@DisplayName("UpdateBatch tests")
public class CheckTransactionOutputTest {

    @Autowired
    private UpdateBatch updateBatch;

    @Autowired
    private UpdateRepository updateRepository;

    @Autowired
    private UpdateService updateService;

    @Test
    @DisplayName("checkTransactionOutput()")
    public void checkTransactionOutput() {
        // =============================================================================================================
        // Test with update number 1 : the transaction output does not exists.
        final Optional<Update> update1 = updateRepository.findById(1L);
        assertTrue(update1.isPresent());
        Assertions.assertEquals(ERROR, updateBatch.checkTransactionOutput(update1.get()));

        Optional<UpdateDTO> updateDTO1 = updateService.getUpdateDTO(1L);
        assertTrue(updateDTO1.isPresent());
        assertEquals(1, updateDTO1.get().getId());
        assertEquals(ERROR, updateDTO1.get().getStatus());
        assertEquals(TRANSACTION_OUTPUT_CHECKED, updateDTO1.get().getFailureStatus());
        assertEquals("Transaction output not found", updateDTO1.get().getFailureMessage());
        assertNotNull(updateDTO1.get().getCreator());
        assertEquals(0, updateDTO1.get().getCreator().getId());
        assertEquals("anonymous", updateDTO1.get().getCreator().getUsername());
        assertEquals(USER, updateDTO1.get().getCreator().getRole());

        // =============================================================================================================
        // Test with update number 2 : the transaction output exists but it's not a taproot.
        final Optional<Update> update2 = updateRepository.findById(2L);
        assertTrue(update2.isPresent());
        Assertions.assertEquals(ERROR, updateBatch.checkTransactionOutput(update2.get()));

        Optional<UpdateDTO> updateDTO2 = updateService.getUpdateDTO(2L);
        assertTrue(updateDTO2.isPresent());
        assertEquals(2, updateDTO2.get().getId());
        assertEquals(ERROR, updateDTO2.get().getStatus());
        assertEquals(TRANSACTION_OUTPUT_CHECKED, updateDTO2.get().getFailureStatus());
        assertEquals("Transaction type is not taproot but p2pkh", updateDTO2.get().getFailureMessage());
        assertNotNull(updateDTO2.get().getCreator());
        assertEquals(0, updateDTO2.get().getCreator().getId());
        assertEquals("anonymous", updateDTO2.get().getCreator().getUsername());
        assertEquals(USER, updateDTO1.get().getCreator().getRole());

        // =============================================================================================================
        // Test with update number 3 : the transaction output is in the blockchain and, it's a taproot one.
        final Optional<Update> update3 = updateRepository.findById(3L);
        assertTrue(update3.isPresent());
        Assertions.assertEquals(TRANSACTION_OUTPUT_CHECKED, updateBatch.checkTransactionOutput(update3.get()));

        Optional<UpdateDTO> updateDTO3 = updateService.getUpdateDTO(3L);
        assertTrue(updateDTO3.isPresent());
        assertEquals(3, updateDTO3.get().getId());
        assertEquals(TRANSACTION_OUTPUT_CHECKED, updateDTO3.get().getStatus());
        assertNull(updateDTO3.get().getFailureStatus());
        assertNull(updateDTO3.get().getFailureMessage());
        assertNotNull(updateDTO3.get().getCreator());
        assertEquals(0, updateDTO3.get().getCreator().getId());
        assertEquals("anonymous", updateDTO3.get().getCreator().getUsername());
        assertEquals(USER, updateDTO1.get().getCreator().getRole());
    }

}
