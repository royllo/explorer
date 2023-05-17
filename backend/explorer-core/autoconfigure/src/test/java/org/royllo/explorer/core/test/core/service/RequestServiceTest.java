package org.royllo.explorer.core.test.core.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.royllo.explorer.core.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.util.constants.UserConstants;
import org.royllo.explorer.core.util.enums.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("RequestService tests")
public class RequestServiceTest {

    @Autowired
    private RequestService requestService;

    @Test
    @Order(1)
    @DisplayName("getOpenedRequests()")
    public void getOpenedRequests() {
        List<RequestDTO> openedRequests = requestService.getOpenedRequests();
        // 4 requests - request n°2 is closed
        assertEquals(3, openedRequests.size());

        // Request 1.
        AddProofRequestDTO request1 = (AddProofRequestDTO) openedRequests.get(0);
        assertEquals(1, request1.getId());
        assertEquals(UserConstants.ANONYMOUS_ID, request1.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID, request1.getCreator().getUserId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, request1.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED, request1.getStatus());
        assertEquals("P1", request1.getRawProof());

        // Request 2.
        AddAssetMetaDataRequestDTO request2 = (AddAssetMetaDataRequestDTO) openedRequests.get(1);
        assertEquals(3, request2.getId());
        assertEquals(UserConstants.ANONYMOUS_ID, request2.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID, request2.getCreator().getUserId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, request2.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED, request2.getStatus());
        assertNull(request2.getErrorMessage());
        assertEquals("AI2", request2.getAssetId());
        assertEquals("MD2", request2.getMetaData());

        // Request 3.
        AddProofRequestDTO request3 = (AddProofRequestDTO) openedRequests.get(2);
        assertEquals(4, request3.getId());
        assertEquals(UserConstants.ANONYMOUS_ID, request3.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID, request3.getCreator().getUserId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, request3.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED, request3.getStatus());
        assertNull(request3.getErrorMessage());
        assertEquals("P4", request3.getRawProof());
    }

    @Test
    @Order(2)
    @DisplayName("getRequestByRequestId()")
    public void getRequestByRequestId() {
        // We should not find this string request id.
        assertTrue(requestService.getRequestByRequestId("NON_EXISTING_REQUEST_ID").isEmpty());

        // We should find this request id already in database.
        assertTrue(requestService.getRequestByRequestId("91425ba6-8b16-46a8-baa6-request_p_02").isPresent());

        // We create a new proof request, and we should find it.
        RequestDTO request1DTO = requestService.createAddProofRequest("proof1");
        assertNotNull(request1DTO);
        assertNotNull(request1DTO.getRequestId());
        assertTrue(requestService.getRequestByRequestId(request1DTO.getRequestId()).isPresent());

        // We create a new medata data request, and we should find it.
        RequestDTO request2DTO = requestService.createAddAssetMetaDataRequest("TaprootAssetId1", "meta1");
        assertNotNull(request2DTO);
        assertNotNull(request2DTO.getRequestId());
        assertTrue(requestService.getRequestByRequestId(request2DTO.getRequestId()).isPresent());
    }

    @Test
    @Order(3)
    @DisplayName("Add requests")
    public void addRequests() {
        // =============================================================================================================
        // Testing data validation.
        try {
            requestService.createAddProofRequest(null);
        } catch (ConstraintViolationException e) {
            final Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
        }

        // =============================================================================================================
        // Request 1 (addAsset).
        RequestDTO request1DTO = requestService.createAddProofRequest("proof1");
        assertNotNull(request1DTO);
        long request1Id = request1DTO.getId();

        // Use getRequest()
        Optional<RequestDTO> request1 = requestService.getRequest(request1Id);
        assertTrue(request1.isPresent());
        assertTrue(request1.get() instanceof AddProofRequestDTO);

        // We cast and check of all the data is here.
        AddProofRequestDTO request1Casted = (AddProofRequestDTO) request1.get();
        assertEquals(request1Id, request1Casted.getId());
        assertNotNull(request1Casted.getRequestId());
        assertEquals(UserConstants.ANONYMOUS_ID, request1Casted.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID, request1Casted.getCreator().getUserId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, request1Casted.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED, request1Casted.getStatus());
        assertNull(request1Casted.getErrorMessage());
        assertEquals("proof1", request1Casted.getRawProof());

        // =============================================================================================================
        // Request 2 (addAssetMetaData).
        RequestDTO request2DTO = requestService.createAddAssetMetaDataRequest("TaprootAssetId1", "meta1");
        assertNotNull(request2DTO);
        long request2Id = request2DTO.getId();

        // Use getRequest()
        Optional<RequestDTO> request2 = requestService.getRequest(request2Id);
        assertTrue(request2.isPresent());
        assertTrue(request2.get() instanceof AddAssetMetaDataRequestDTO);

        // We cast and check of all the data is here.
        AddAssetMetaDataRequestDTO request2Casted = (AddAssetMetaDataRequestDTO) request2.get();
        assertEquals(request2Id, request2Casted.getId());
        assertNotNull(request2Casted.getRequestId());
        assertEquals(UserConstants.ANONYMOUS_ID, request2Casted.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID, request2Casted.getCreator().getUserId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, request2Casted.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED, request2Casted.getStatus());
        assertNull(request2Casted.getErrorMessage());
        assertEquals("TaprootAssetId1", request2Casted.getAssetId());
        assertEquals("meta1", request2Casted.getMetaData());

        // =============================================================================================================
        // Request 3 (addAsset).
        RequestDTO request3DTO = requestService.createAddProofRequest("proof2");
        assertNotNull(request3DTO);
        long request3Id = request3DTO.getId();

        // Use getRequest()
        Optional<RequestDTO> request3 = requestService.getRequest(request3Id);
        assertTrue(request3.isPresent());
        assertTrue(request3.get() instanceof AddProofRequestDTO);

        // We cast and check of all the data is here.
        AddProofRequestDTO request3Casted = (AddProofRequestDTO) request3.get();
        assertEquals(request3Id, request3Casted.getId());
        assertNotNull(request3Casted.getRequestId());
        assertEquals(UserConstants.ANONYMOUS_ID, request3Casted.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID, request3Casted.getCreator().getUserId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, request3Casted.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED, request3Casted.getStatus());
        assertNull(request3Casted.getErrorMessage());
        assertEquals("proof2", request3Casted.getRawProof());
    }

}
