package org.royllo.explorer.core.test.core.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.domain.request.AddAssetMetaDataRequest;
import org.royllo.explorer.core.domain.request.AddAssetRequest;
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
        Optional<User> user = userRepository.findById(UserConstants.ANONYMOUS_USER_ID);
        assertTrue(user.isPresent());

        // =============================================================================================================
        // Creating request 1 (ADD_ASSET).
        AddAssetRequest request1 = new AddAssetRequest();
        request1.setCreator(user.get());
        request1.setStatus(RequestStatus.OPENED);
        request1.setGenesisPoint("0:1");
        request1.setName("name01");
        request1.setMetaData("metaData01");
        request1.setAssetId("assetId01");
        request1.setOutputIndex(1);
        request1.setProof("Proof1");
        long request1ID = requestRepository.save(request1).getId();
        logger.info("Request 1 ID is " + request1ID);

        // See what's in database with JPA.
        Optional<Request> request1FromJPA = requestRepository.findById(request1ID);
        assertTrue(request1FromJPA.isPresent());
        AddAssetRequest addAssetRequest1FromJPA = (AddAssetRequest) request1FromJPA.get();
        assertEquals(request1ID, addAssetRequest1FromJPA.getId());
        Assertions.assertEquals("anonymous", addAssetRequest1FromJPA.getCreator().getUsername());
        Assertions.assertEquals(RequestStatus.OPENED, addAssetRequest1FromJPA.getStatus());
        assertNull(addAssetRequest1FromJPA.getErrorMessage());
        assertEquals("0:1", addAssetRequest1FromJPA.getGenesisPoint());
        assertEquals("name01", addAssetRequest1FromJPA.getName());
        assertEquals("metaData01", addAssetRequest1FromJPA.getMetaData());
        assertEquals("assetId01", addAssetRequest1FromJPA.getAssetId());
        assertEquals(1, addAssetRequest1FromJPA.getOutputIndex());
        assertEquals("Proof1", addAssetRequest1FromJPA.getProof());

        // See what's in database with JDBC.
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Long request1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS", Long.class);
        assertEquals(request1ID, request1IDFromJDBC);
        Long addAssetRequest1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS_ADD_ASSET", Long.class);
        assertEquals(request1ID, addAssetRequest1IDFromJDBC);

        // =============================================================================================================
        // Creating request 2 (ADD_ASSET_META_DATA).
        AddAssetMetaDataRequest request2 = new AddAssetMetaDataRequest();
        request2.setCreator(user.get());
        request2.setStatus(RequestStatus.SUCCESS);
        request2.setAssetId("TaroAssetId1");
        request2.setMetaData("Meta1");
        long request2ID = requestRepository.save(request2).getId();
        logger.info("Request 2 ID is " + request2ID);

        // See what's in database with JPA.
        Optional<Request> request2FromJPA = requestRepository.findById(request2ID);
        assertTrue(request2FromJPA.isPresent());
        AddAssetMetaDataRequest addAssetMeatRequest2FromJPA = (AddAssetMetaDataRequest) request2FromJPA.get();
        assertEquals(request2ID, addAssetMeatRequest2FromJPA.getId());
        Assertions.assertEquals("anonymous", addAssetMeatRequest2FromJPA.getCreator().getUsername());
        Assertions.assertEquals(RequestStatus.SUCCESS, addAssetMeatRequest2FromJPA.getStatus());
        assertNull(addAssetMeatRequest2FromJPA.getErrorMessage());
        assertEquals("TaroAssetId1", addAssetMeatRequest2FromJPA.getAssetId());
        assertEquals("Meta1", addAssetMeatRequest2FromJPA.getMetaData());

        // See what's in database with JDBC.
        jdbcTemplate = new JdbcTemplate(dataSource);
        Long request2IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS", Long.class);
        assertEquals(request2ID, request2IDFromJDBC);
        Long addAssetRequest2IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS_ADD_ASSET_META_DATA", Long.class);
        assertEquals(request2ID, addAssetRequest2IDFromJDBC);

        // =============================================================================================================
        // Creating request 3 (ADD_ASSET).
        AddAssetRequest request3 = new AddAssetRequest();
        request3.setCreator(user.get());
        request3.setStatus(RequestStatus.FAILURE);
        request3.setGenesisPoint("0:2");
        request3.setName("name02");
        request3.setMetaData("metaData02");
        request3.setAssetId("assetId02");
        request3.setOutputIndex(2);
        request3.setProof("Proof2");
        long request3ID = requestRepository.save(request3).getId();
        logger.info("Request 3 ID is " + request3ID);

        // See what's in database with JPA.
        Optional<Request> request3FromJPA = requestRepository.findById(request3ID);
        assertTrue(request3FromJPA.isPresent());
        AddAssetRequest addAssetRequest2FromJPA = (AddAssetRequest) request3FromJPA.get();
        assertEquals(request3ID, addAssetRequest2FromJPA.getId());
        Assertions.assertEquals("anonymous", addAssetRequest2FromJPA.getCreator().getUsername());
        Assertions.assertEquals(RequestStatus.FAILURE, addAssetRequest2FromJPA.getStatus());
        assertNull(addAssetRequest2FromJPA.getErrorMessage());
        assertEquals("0:2", addAssetRequest2FromJPA.getGenesisPoint());
        assertEquals("name02", addAssetRequest2FromJPA.getName());
        assertEquals("metaData02", addAssetRequest2FromJPA.getMetaData());
        assertEquals("assetId02", addAssetRequest2FromJPA.getAssetId());
        assertEquals(2, addAssetRequest2FromJPA.getOutputIndex());
        assertEquals("Proof2", addAssetRequest2FromJPA.getProof());

        // See what's in database with JDBC.
        jdbcTemplate = new JdbcTemplate(dataSource);
        Long request3IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS", Long.class);
        assertEquals(request3ID, request3IDFromJDBC);
        Long addAssetRequest3IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS_ADD_ASSET", Long.class);
        assertEquals(request3ID, addAssetRequest3IDFromJDBC);
    }

}
