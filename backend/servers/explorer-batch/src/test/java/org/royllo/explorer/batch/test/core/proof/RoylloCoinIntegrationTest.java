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
import static org.royllo.test.TapdData.ROYLLO_COIN_FROM_TEST;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DisplayName("roylloCoin integration test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"scheduler-disabled"})
public class RoylloCoinIntegrationTest extends TestWithMockServers {

    @Autowired
    RequestService requestService;

    @Autowired
    AddProofBatch addProofBatch;

    @Test
    @DisplayName("Process proof")
    public void processProof() {
        // roylloCoin already exists in our database, so we should not be able to add it again.
        final String ROYLLO_COIN_RAW_PROOF = ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String ROYLLO_COIN_PROOF_ID = sha256(ROYLLO_COIN_RAW_PROOF);

        // We create the request
        AddProofRequestDTO myRoylloCoinRequest = requestService.createAddProofRequest(ROYLLO_COIN_RAW_PROOF);
        assertNotNull(myRoylloCoinRequest);
        assertEquals(OPENED, myRoylloCoinRequest.getStatus());

        // WeProcess the request - We should have a failure as the proof already exists.
        addProofBatch.processRequests();
        final Optional<RequestDTO> myRoylloCoinRequestTreated = requestService.getRequest(myRoylloCoinRequest.getId());
        assertTrue(myRoylloCoinRequestTreated.isPresent());
        assertFalse(myRoylloCoinRequestTreated.get().isSuccessful());
        assertEquals(FAILURE, myRoylloCoinRequestTreated.get().getStatus());
        assertEquals("This proof is already registered with proof id: " + ROYLLO_COIN_PROOF_ID, myRoylloCoinRequestTreated.get().getErrorMessage());
    }

}
