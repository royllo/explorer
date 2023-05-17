package org.royllo.explorer.core.test.core.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.domain.request.AddAssetMetaDataRequest;
import org.royllo.explorer.core.domain.request.AddProof;
import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.test.util.BaseTest;
import org.royllo.explorer.core.util.constants.UserConstants;
import org.royllo.explorer.core.util.enums.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayName("RequestRepository tests")
public class RequestRepositoryTest extends BaseTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    @DisplayName("Create requests")
    public void createRequests() {
        // This test is specifically designed to test the table inheritance.
        Optional<User> user = userRepository.findById(UserConstants.ANONYMOUS_ID);
        assertTrue(user.isPresent());

        // =============================================================================================================
        // Creating request 1 (ADD_ASSET).
        AddProof request1 = new AddProof();
        request1.setRequestId(UUID.randomUUID().toString());
        request1.setCreator(user.get());
        request1.setStatus(RequestStatus.OPENED);
        request1.setRawProof("Proof1");
        long request1ID = requestRepository.save(request1).getId();

        // See what's in database with JPA.
        Optional<Request> request1FromJPA = requestRepository.findById(request1ID);
        assertTrue(request1FromJPA.isPresent());
        AddProof addProof1FromJPA = (AddProof) request1FromJPA.get();
        assertEquals(request1ID, addProof1FromJPA.getId());
        Assertions.assertEquals("anonymous", addProof1FromJPA.getCreator().getUsername());
        Assertions.assertEquals(RequestStatus.OPENED, addProof1FromJPA.getStatus());
        assertNull(addProof1FromJPA.getErrorMessage());
        assertEquals("Proof1", addProof1FromJPA.getRawProof());

        // See what's in database with JDBC.
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Long request1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS", Long.class);
        assertEquals(request1ID, request1IDFromJDBC);
        Long addAssetRequest1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS_ADD_PROOF", Long.class);
        assertEquals(request1ID, addAssetRequest1IDFromJDBC);

        // =============================================================================================================
        // Creating request 2 (ADD_ASSET_META_DATA).
        AddAssetMetaDataRequest request2 = new AddAssetMetaDataRequest();
        request2.setRequestId(UUID.randomUUID().toString());
        request2.setCreator(user.get());
        request2.setStatus(RequestStatus.SUCCESS);
        request2.setAssetId("TaprootAssetId1");
        request2.setMetaData("Meta1");
        long request2ID = requestRepository.save(request2).getId();

        // See what's in database with JPA.
        Optional<Request> request2FromJPA = requestRepository.findById(request2ID);
        assertTrue(request2FromJPA.isPresent());
        AddAssetMetaDataRequest addAssetMeatRequest2FromJPA = (AddAssetMetaDataRequest) request2FromJPA.get();
        assertEquals(request2ID, addAssetMeatRequest2FromJPA.getId());
        Assertions.assertEquals("anonymous", addAssetMeatRequest2FromJPA.getCreator().getUsername());
        Assertions.assertEquals(RequestStatus.SUCCESS, addAssetMeatRequest2FromJPA.getStatus());
        assertNull(addAssetMeatRequest2FromJPA.getErrorMessage());
        assertEquals("TaprootAssetId1", addAssetMeatRequest2FromJPA.getAssetId());
        assertEquals("Meta1", addAssetMeatRequest2FromJPA.getMetaData());

        // See what's in database with JDBC.
        jdbcTemplate = new JdbcTemplate(dataSource);
        Long request2IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS", Long.class);
        assertEquals(request2ID, request2IDFromJDBC);
        Long addAssetRequest2IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS_ADD_ASSET_META_DATA", Long.class);
        assertEquals(request2ID, addAssetRequest2IDFromJDBC);

        // =============================================================================================================
        // Creating request 3 (ADD_ASSET).
        AddProof request3 = new AddProof();
        request3.setRequestId(UUID.randomUUID().toString());
        request3.setCreator(user.get());
        request3.setStatus(RequestStatus.FAILURE);
        request3.setRawProof("Proof2");
        long request3ID = requestRepository.save(request3).getId();

        // See what's in database with JPA.
        Optional<Request> request3FromJPA = requestRepository.findById(request3ID);
        assertTrue(request3FromJPA.isPresent());
        AddProof addProof2FromJPA = (AddProof) request3FromJPA.get();
        assertEquals(request3ID, addProof2FromJPA.getId());
        Assertions.assertEquals("anonymous", addProof2FromJPA.getCreator().getUsername());
        Assertions.assertEquals(RequestStatus.FAILURE, addProof2FromJPA.getStatus());
        assertNull(addProof2FromJPA.getErrorMessage());
        assertEquals("Proof2", addProof2FromJPA.getRawProof());

        // See what's in database with JDBC.
        jdbcTemplate = new JdbcTemplate(dataSource);
        Long request3IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS", Long.class);
        assertEquals(request3ID, request3IDFromJDBC);
        Long addAssetRequest3IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS_ADD_PROOF", Long.class);
        assertEquals(request3ID, addAssetRequest3IDFromJDBC);
    }

}
