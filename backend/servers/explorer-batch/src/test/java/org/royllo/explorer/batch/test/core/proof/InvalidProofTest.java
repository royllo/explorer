package org.royllo.explorer.batch.test.core.proof;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DisplayName("Invalid proof test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"scheduler-disabled"})
public class InvalidProofTest extends TestWithMockServers {

    @Autowired
    RequestService requestService;

    @Autowired
    AddProofBatch addProofBatch;

    @Test
    @DisplayName("Process proof")
    public void processProof() {
        // We add an invalid proof that can't be decoded ("INVALID_PROOF").

        // Add the proof
        AddProofRequestDTO invalidProofRequest = requestService.createAddProofRequest("INVALID_PROOF");
        assertNotNull(invalidProofRequest);
        assertEquals(OPENED, invalidProofRequest.getStatus());

        // Process the request - Check the produced error.
        addProofBatch.processRequests();
        final Optional<RequestDTO> invalidProofRequestTreated = requestService.getRequest(invalidProofRequest.getId());
        assertTrue(invalidProofRequestTreated.isPresent());
        assertFalse(invalidProofRequestTreated.get().isSuccessful());
        assertEquals(FAILURE, invalidProofRequestTreated.get().getStatus());
        assertEquals("proto: (line 1:17): invalid value for bytes type: \"INVALID_PROOF\"", invalidProofRequestTreated.get().getErrorMessage());
    }

}
