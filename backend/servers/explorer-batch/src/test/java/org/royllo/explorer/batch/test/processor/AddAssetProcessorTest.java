package org.royllo.explorer.batch.test.processor;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.service.RequestProcessorService;
import org.royllo.explorer.batch.test.util.BaseTest;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@Disabled
@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"mempoolTransactionServiceMock", "tarodProofServiceMock", "scheduler-disabled"})
public class AddAssetProcessorTest extends BaseTest {

    @Autowired
    RequestService requestService;

    @Autowired
    RequestProcessorService requestProcessorService;

    @Test
    @DisplayName("Process")
    public void process() {
        // =============================================================================================================
        // Adding three requests and run batch.

        // My Royllo coin.
        AddProofRequestDTO myRoylloCoinRequest = requestService.addProof(MY_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(myRoylloCoinRequest);
        assertNotNull(myRoylloCoinRequest.getRequestId());
        String myRoylloCoinRequestId = myRoylloCoinRequest.getRequestId();

        // Known Royllo coin.
        AddProofRequestDTO knownRoylloCoinRequest = requestService.addProof(KNOWN_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(knownRoylloCoinRequest);
        assertNotNull(knownRoylloCoinRequest.getRequestId());
        String knownRoylloCoinRequestId = knownRoylloCoinRequest.getRequestId();

        // Unknown Royllo coin.
        AddProofRequestDTO unknownRoylloCoinRequest = requestService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(unknownRoylloCoinRequest);
        assertNotNull(unknownRoylloCoinRequest.getRequestId());
        String unknownRoylloCoinRequestId = unknownRoylloCoinRequest.getRequestId();

        // Active Royllo coin - Proof 1.
        AddProofRequestDTO activeRoylloCoinRequest1 = requestService.addProof(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF);
        assertNotNull(activeRoylloCoinRequest1);
        assertNotNull(activeRoylloCoinRequest1.getRequestId());
        String activeRoylloCoinRequestId = activeRoylloCoinRequest1.getRequestId();

        // =============================================================================================================
        // My Royllo coin.

        // =============================================================================================================
        // Known Royllo coin.

        // =============================================================================================================
        // Unknown Royllo coin.

        // =============================================================================================================
        // Active Royllo coin.

    }

}
