package org.royllo.explorer.api.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.dto.request.AddAssetMetaRequestDTO;
import org.royllo.explorer.api.dto.request.AddAssetRequestDTO;
import org.royllo.explorer.api.dto.request.RequestDTO;
import org.royllo.explorer.api.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static graphql.Assert.assertNull;
import static graphql.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.royllo.explorer.api.util.constants.UserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.api.util.constants.UserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.api.util.enums.RequestStatus.NEW;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DisplayName("RequestService tests")
public class RequestServiceTest {

    @Autowired
    private RequestService requestService;

    @Test
    @DisplayName("Add requests")
    public void addRequests() {
        // =============================================================================================================
        // Request 1 (addAsset).
        RequestDTO request1DTO = requestService.addAsset("genesis1", "proof1");
        assertNotNull(request1DTO);
        long request1Id = request1DTO.getId();

        // Use getRequest()
        Optional<RequestDTO> request1 = requestService.getRequest(request1Id);
        assertTrue(request1.isPresent());
        assertTrue(request1.get() instanceof AddAssetRequestDTO);

        // We cast and check of all the data is here.
        AddAssetRequestDTO request1Casted = (AddAssetRequestDTO) request1.get();
        assertEquals(request1Id, request1Casted.getId());
        assertEquals(ANONYMOUS_USER_ID, request1Casted.getCreator().getId());
        assertEquals(ANONYMOUS_USER_USERNAME, request1Casted.getCreator().getUsername());
        assertEquals(NEW, request1Casted.getStatus());
        assertNull(request1Casted.getErrorMessage());
        assertEquals("genesis1", request1Casted.getGenesisBootstrapInformation());
        assertEquals("proof1", request1Casted.getProof());

        // =============================================================================================================
        // Request 2 (addAssetMeta).
        RequestDTO request2DTO = requestService.addAssetMeta("taroAssetId1", "meta1");
        assertNotNull(request2DTO);
        long request2Id = request2DTO.getId();

        // Use getRequest()
        Optional<RequestDTO> request2 = requestService.getRequest(request2Id);
        assertTrue(request2.isPresent());
        assertTrue(request2.get() instanceof AddAssetMetaRequestDTO);

        // We cast and check of all the data is here.
        AddAssetMetaRequestDTO request2Casted = (AddAssetMetaRequestDTO) request2.get();
        assertEquals(request2Id, request2Casted.getId());
        assertEquals(ANONYMOUS_USER_ID, request2Casted.getCreator().getId());
        assertEquals(ANONYMOUS_USER_USERNAME, request2Casted.getCreator().getUsername());
        assertEquals(NEW, request2Casted.getStatus());
        assertNull(request2Casted.getErrorMessage());
        assertEquals("taroAssetId1", request2Casted.getTaroAssetId());
        assertEquals("meta1", request2Casted.getMeta());

        // =============================================================================================================
        // Request 3 (addAsset).
        RequestDTO request3DTO = requestService.addAsset("genesis2", "proof2");
        assertNotNull(request3DTO);
        long request3Id = request3DTO.getId();

        // Use getRequest()
        Optional<RequestDTO> request3 = requestService.getRequest(request3Id);
        assertTrue(request3.isPresent());
        assertTrue(request3.get() instanceof AddAssetRequestDTO);

        // We cast and check of all the data is here.
        AddAssetRequestDTO request3Casted = (AddAssetRequestDTO) request3.get();
        assertEquals(request3Id, request3Casted.getId());
        assertEquals(ANONYMOUS_USER_ID, request3Casted.getCreator().getId());
        assertEquals(ANONYMOUS_USER_USERNAME, request3Casted.getCreator().getUsername());
        assertEquals(NEW, request3Casted.getStatus());
        assertNull(request3Casted.getErrorMessage());
        assertEquals("genesis2", request3Casted.getGenesisBootstrapInformation());
        assertEquals("proof2", request3Casted.getProof());
    }

}
