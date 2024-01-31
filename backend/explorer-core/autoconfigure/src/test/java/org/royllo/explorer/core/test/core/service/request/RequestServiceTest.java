package org.royllo.explorer.core.test.core.service.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.AddUniverseServerRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.core.util.enums.ProofType.PROOF_TYPE_ISSUANCE;
import static org.royllo.explorer.core.util.enums.ProofType.PROOF_TYPE_TRANSFER;
import static org.royllo.explorer.core.util.enums.ProofType.PROOF_TYPE_UNSPECIFIED;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;

@SpringBootTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("RequestService tests")
public class RequestServiceTest {

    @Autowired
    private RequestService requestService;

    @Test
    @Order(1)
    @DisplayName("getOpenedRequests()")
    public void getOpenedRequests() {
        List<RequestDTO> openedRequests = requestService.getOpenedRequests();
        // 4 requests - request n°2 is closed => 3 opened requests.
        assertEquals(3, openedRequests.size());

        // Request 1.
        AddProofRequestDTO request1 = (AddProofRequestDTO) openedRequests.get(0);
        assertEquals(1, request1.getId());
        assertEquals(ANONYMOUS_ID, request1.getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, request1.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, request1.getCreator().getUsername());
        assertEquals(OPENED, request1.getStatus());
        assertEquals("P1", request1.getProof());

        // Request 2.
        AddUniverseServerRequestDTO request2 = (AddUniverseServerRequestDTO) openedRequests.get(1);
        assertEquals(3, request2.getId());
        assertEquals(ANONYMOUS_ID, request2.getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, request2.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, request2.getCreator().getUsername());
        assertEquals(OPENED, request2.getStatus());
        assertNull(request2.getErrorMessage());
        assertEquals("SERVER_3", request2.getServerAddress());

        // Request 3.
        AddProofRequestDTO request3 = (AddProofRequestDTO) openedRequests.get(2);
        assertEquals(4, request3.getId());
        assertEquals(ANONYMOUS_ID, request3.getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, request3.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, request3.getCreator().getUsername());
        assertEquals(OPENED, request3.getStatus());
        assertNull(request3.getErrorMessage());
        assertEquals("P4", request3.getProof());
    }

    @Test
    @Order(2)
    @DisplayName("getRequestByRequestId()")
    public void getRequestByRequestId() {
        // We should not find this string request id.
        assertTrue(requestService.getRequestByRequestId("NON_EXISTING_REQUEST_ID").isEmpty());

        // We should find this request id already in database.
        assertTrue(requestService.getRequestByRequestId("91425ba6-8b16-46a8-baa6-request_p_02").isPresent());

        // We create a new "add proof" request, and we should find it.
        RequestDTO request1DTO = requestService.createAddProofRequest("proof1");
        assertNotNull(request1DTO);
        assertNotNull(request1DTO.getRequestId());
        assertTrue(requestService.getRequestByRequestId(request1DTO.getRequestId()).isPresent());

        // We create a new "add medata data" request, and we should find it.
        RequestDTO request2DTO = requestService.createAddUniverseServerRequest("server4");
        assertNotNull(request2DTO);
        assertNotNull(request2DTO.getRequestId());
        assertTrue(requestService.getRequestByRequestId(request2DTO.getRequestId()).isPresent());
    }

    @Test
    @Order(3)
    @DisplayName("Add requests")
    public void addRequests() {
        // =============================================================================================================
        // Request 1 (addAssetDTO).
        RequestDTO request1DTO = requestService.createAddProofRequest("proof1");
        assertNotNull(request1DTO);
        long request1Id = request1DTO.getId();

        // Use getRequest().
        Optional<RequestDTO> request1 = requestService.getRequest(request1Id);
        assertTrue(request1.isPresent());
        assertInstanceOf(AddProofRequestDTO.class, request1.get());

        // We cast and check of all the data is here.
        AddProofRequestDTO request1Casted = (AddProofRequestDTO) request1.get();
        assertEquals(request1Id, request1Casted.getId());
        assertNotNull(request1Casted.getRequestId());
        assertEquals(ANONYMOUS_ID, request1Casted.getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, request1Casted.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, request1Casted.getCreator().getUsername());
        assertEquals(OPENED, request1Casted.getStatus());
        assertNull(request1Casted.getErrorMessage());
        assertEquals("proof1", request1Casted.getProof());
        assertEquals(PROOF_TYPE_UNSPECIFIED, request1Casted.getProofType());

        // =============================================================================================================
        // Request 2 (addAssetMetaData).
        RequestDTO request2DTO = requestService.createAddUniverseServerRequest("universe1");
        assertNotNull(request2DTO);
        long request2Id = request2DTO.getId();

        // Use getRequest().
        Optional<RequestDTO> request2 = requestService.getRequest(request2Id);
        assertTrue(request2.isPresent());
        assertInstanceOf(AddUniverseServerRequestDTO.class, request2.get());

        // We cast and check of all the data is here.
        AddUniverseServerRequestDTO request2Casted = (AddUniverseServerRequestDTO) request2.get();
        assertEquals(request2Id, request2Casted.getId());
        assertNotNull(request2Casted.getRequestId());
        assertEquals(ANONYMOUS_ID, request2Casted.getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, request2Casted.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, request2Casted.getCreator().getUsername());
        assertEquals(OPENED, request2Casted.getStatus());
        assertNull(request2Casted.getErrorMessage());
        assertEquals("universe1", request2Casted.getServerAddress());

        // =============================================================================================================
        // Request 3 (addAssetDTO).
        RequestDTO request3DTO = requestService.createAddProofRequest("proof2");
        assertNotNull(request3DTO);
        long request3Id = request3DTO.getId();

        // Use getRequest().
        Optional<RequestDTO> request3 = requestService.getRequest(request3Id);
        assertTrue(request3.isPresent());
        assertInstanceOf(AddProofRequestDTO.class, request3.get());

        // We cast and check of all the data is here.
        AddProofRequestDTO request3Casted = (AddProofRequestDTO) request3.get();
        assertEquals(request3Id, request3Casted.getId());
        assertNotNull(request3Casted.getRequestId());
        assertEquals(ANONYMOUS_ID, request3Casted.getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, request3Casted.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, request3Casted.getCreator().getUsername());
        assertEquals(OPENED, request3Casted.getStatus());
        assertNull(request3Casted.getErrorMessage());
        assertEquals("proof2", request3Casted.getProof());
        assertEquals(PROOF_TYPE_UNSPECIFIED, request3Casted.getProofType());

        // =============================================================================================================
        // Request 4 (AddUniverseServerRequest).
        RequestDTO request4DTO = requestService.createAddUniverseServerRequest("1.1.1.1:8080");
        assertNotNull(request4DTO);
        long request4Id = request4DTO.getId();

        // Use getRequest().
        Optional<RequestDTO> request4 = requestService.getRequest(request4Id);
        assertTrue(request4.isPresent());
        assertInstanceOf(AddUniverseServerRequestDTO.class, request4.get());

        // We cast and check of all the data is here.
        AddUniverseServerRequestDTO request4Casted = (AddUniverseServerRequestDTO) request4.get();
        assertEquals(request4Id, request4Casted.getId());
        assertNotNull(request4Casted.getRequestId());
        assertEquals(ANONYMOUS_ID, request4Casted.getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, request4Casted.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, request4Casted.getCreator().getUsername());
        assertEquals(OPENED, request4Casted.getStatus());
        assertNull(request4Casted.getErrorMessage());
        assertEquals("1.1.1.1:8080", request4Casted.getServerAddress());

        // Testing createAddProofRequest(String proof, ProofType proofType).
        AddProofRequestDTO request5DTO = requestService.createAddProofRequest("proof", PROOF_TYPE_ISSUANCE);
        assertNotNull(request5DTO.getId());
        assertEquals(PROOF_TYPE_ISSUANCE, request5DTO.getProofType());

        AddProofRequestDTO request6DTO = requestService.createAddProofRequest("proof", PROOF_TYPE_TRANSFER);
        assertNotNull(request6DTO.getId());
        assertEquals(PROOF_TYPE_TRANSFER, request6DTO.getProofType());
    }

}
