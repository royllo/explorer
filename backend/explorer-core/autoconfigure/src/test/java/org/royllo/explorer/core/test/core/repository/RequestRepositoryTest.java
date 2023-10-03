package org.royllo.explorer.core.test.core.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.domain.request.AddAssetMetaDataRequest;
import org.royllo.explorer.core.domain.request.AddProofRequest;
import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;

@SpringBootTest
@DisplayName("RequestRepository tests")
public class RequestRepositoryTest extends TestWithMockServers {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    @DisplayName("Requests inheritance")
    public void requestsInheritance() {
        // =============================================================================================================
        // Creating request 1 (ADD_ASSET).
        AddProofRequest request1 = new AddProofRequest();
        request1.setRequestId(UUID.randomUUID().toString());
        request1.setCreator(ANONYMOUS_USER);
        request1.setStatus(OPENED);
        request1.setRawProof("Proof1");
        long request1ID = requestRepository.save(request1).getId();

        // See what's in database with JPA.
        Optional<Request> request1FromJPA = requestRepository.findById(request1ID);
        assertTrue(request1FromJPA.isPresent());
        AddProofRequest addProofRequest1FromJPA = (AddProofRequest) request1FromJPA.get();
        assertEquals(request1ID, addProofRequest1FromJPA.getId());
        assertEquals(ANONYMOUS_USER_USERNAME, addProofRequest1FromJPA.getCreator().getUsername());
        assertEquals(OPENED, addProofRequest1FromJPA.getStatus());
        assertNull(addProofRequest1FromJPA.getErrorMessage());
        assertEquals("Proof1", addProofRequest1FromJPA.getRawProof());

        // See what's in database with JDBC.
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Long request1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST", Long.class);
        assertEquals(request1ID, request1IDFromJDBC);
        Long addAssetRequest1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST_ADD_PROOF", Long.class);
        assertEquals(request1ID, addAssetRequest1IDFromJDBC);

        // =============================================================================================================
        // Creating request 2 (ADD_ASSET_META_DATA).
        AddAssetMetaDataRequest request2 = new AddAssetMetaDataRequest();
        request2.setRequestId(UUID.randomUUID().toString());
        request2.setCreator(ANONYMOUS_USER);
        request2.setStatus(SUCCESS);
        request2.setAssetId("TaprootAssetId1");
        request2.setMetaData("Meta1");
        long request2ID = requestRepository.save(request2).getId();

        // See what's in database with JPA.
        Optional<Request> request2FromJPA = requestRepository.findById(request2ID);
        assertTrue(request2FromJPA.isPresent());
        AddAssetMetaDataRequest addAssetMeatRequest2FromJPA = (AddAssetMetaDataRequest) request2FromJPA.get();
        assertEquals(request2ID, addAssetMeatRequest2FromJPA.getId());
        assertEquals(ANONYMOUS_USER_USERNAME, addAssetMeatRequest2FromJPA.getCreator().getUsername());
        assertEquals(SUCCESS, addAssetMeatRequest2FromJPA.getStatus());
        assertNull(addAssetMeatRequest2FromJPA.getErrorMessage());
        assertEquals("TaprootAssetId1", addAssetMeatRequest2FromJPA.getAssetId());
        assertEquals("Meta1", addAssetMeatRequest2FromJPA.getMetaData());

        // See what's in database with JDBC.
        jdbcTemplate = new JdbcTemplate(dataSource);
        Long request2IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST", Long.class);
        assertEquals(request2ID, request2IDFromJDBC);
        Long addAssetRequest2IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST_ADD_ASSET_META_DATA", Long.class);
        assertEquals(request2ID, addAssetRequest2IDFromJDBC);

        // =============================================================================================================
        // Creating request 3 (ADD_ASSET).
        AddProofRequest request3 = new AddProofRequest();
        request3.setRequestId(UUID.randomUUID().toString());
        request3.setCreator(ANONYMOUS_USER);
        request3.setStatus(FAILURE);
        request3.setRawProof("Proof2");
        long request3ID = requestRepository.save(request3).getId();

        // See what's in database with JPA.
        Optional<Request> request3FromJPA = requestRepository.findById(request3ID);
        assertTrue(request3FromJPA.isPresent());
        AddProofRequest addProofRequest2FromJPA = (AddProofRequest) request3FromJPA.get();
        assertEquals(request3ID, addProofRequest2FromJPA.getId());
        assertEquals(ANONYMOUS_USER_USERNAME, addProofRequest2FromJPA.getCreator().getUsername());
        assertEquals(FAILURE, addProofRequest2FromJPA.getStatus());
        assertNull(addProofRequest2FromJPA.getErrorMessage());
        assertEquals("Proof2", addProofRequest2FromJPA.getRawProof());

        // See what's in database with JDBC.
        jdbcTemplate = new JdbcTemplate(dataSource);
        Long request3IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST", Long.class);
        assertEquals(request3ID, request3IDFromJDBC);
        Long addAssetRequest3IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST_ADD_PROOF", Long.class);
        assertEquals(request3ID, addAssetRequest3IDFromJDBC);
    }

}
