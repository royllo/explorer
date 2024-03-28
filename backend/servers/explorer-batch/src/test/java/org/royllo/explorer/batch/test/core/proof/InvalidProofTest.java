package org.royllo.explorer.batch.test.core.proof;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;

@SpringBootTest
@DirtiesContext
@DisplayName("Invalid proof test")
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
        AddProofRequestDTO invalidProofRequest = requestService.createAddProofRequest("INVALID_PROOF");
        assertNotNull(invalidProofRequest);
        assertEquals(OPENED, invalidProofRequest.getStatus());

        // Process the request - Check the request error.
        addProofBatch.processRequests();
        assertThat(requestService.getRequest(invalidProofRequest.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertFalse(request.isSuccessful());
                    assertEquals(FAILURE, request.getStatus());
                    assertThat(request.getErrorMessage()).contains("invalid value for bytes type: \"INVALID_PROOF\"");
                });
    }

}
