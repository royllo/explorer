package org.royllo.explorer.api.test.core.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.domain.request.AddAssetMetaRequest;
import org.royllo.explorer.api.domain.request.AddAssetRequest;
import org.royllo.explorer.api.domain.request.Request;
import org.royllo.explorer.api.domain.user.User;
import org.royllo.explorer.api.repository.request.RequestRepository;
import org.royllo.explorer.api.repository.user.UserRepository;
import org.royllo.explorer.api.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.api.util.constants.UserConstants.ANONYMOUS_USER_UID;
import static org.royllo.explorer.api.util.enums.RequestStatus.NEW;
import static org.royllo.explorer.api.util.enums.RequestStatus.SUCCESS;

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
    @DisplayName("Creating requests")
    public void createRequests() {
        Optional<User> user = userRepository.findById(ANONYMOUS_USER_UID);
        assertTrue(user.isPresent());

        // =============================================================================================================
        // Creating request 1 (ADD_ASSET).
        AddAssetRequest request1 = new AddAssetRequest();
        request1.setCreator(user.get());
        request1.setStatus(NEW);
        request1.setGenesisBootstrapInformation("Genesis1");
        request1.setProof("Proof1");
        long request1ID = requestRepository.save(request1).getId();

        // See what's in database with JPA.
        Optional<Request> request1FromJPA = requestRepository.findById(request1ID);
        assertTrue(request1FromJPA.isPresent());
        AddAssetRequest addAssetRequest1FromJPA = (AddAssetRequest) request1FromJPA.get();
        assertEquals(request1ID, addAssetRequest1FromJPA.getId());
        assertEquals("anonymous", addAssetRequest1FromJPA.getCreator().getUsername());
        assertEquals(NEW, addAssetRequest1FromJPA.getStatus());
        assertNull(addAssetRequest1FromJPA.getErrorMessage());
        assertEquals("Genesis1", addAssetRequest1FromJPA.getGenesisBootstrapInformation());
        assertEquals("Proof1", addAssetRequest1FromJPA.getProof());

        // See what's in database with JDBC.
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Long request1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS", Long.class);
        assertEquals(request1ID, request1IDFromJDBC);
        Long addAssetRequest1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS_ADD_ASSET", Long.class);
        assertEquals(request1ID, addAssetRequest1IDFromJDBC);

        // =============================================================================================================
        // Creating request 2 (ADD_ASSET_META_DATA).
        AddAssetMetaRequest request2 = new AddAssetMetaRequest();
        request2.setCreator(user.get());
        request2.setStatus(SUCCESS);
        request2.setTaroAssetId("TaroAssetId1");
        request2.setMeta("Meta1");
        long request2ID = requestRepository.save(request2).getId();

        // See what's in database with JPA.
        Optional<Request> request2FromJPA = requestRepository.findById(request2ID);
        assertTrue(request2FromJPA.isPresent());
        AddAssetMetaRequest addAssetMeatRequest2FromJPA = (AddAssetMetaRequest) request2FromJPA.get();
        assertEquals(request2ID, addAssetMeatRequest2FromJPA.getId());
        assertEquals("anonymous", addAssetMeatRequest2FromJPA.getCreator().getUsername());
        assertEquals(SUCCESS, addAssetMeatRequest2FromJPA.getStatus());
        assertNull(addAssetMeatRequest2FromJPA.getErrorMessage());
        assertEquals("TaroAssetId1", addAssetMeatRequest2FromJPA.getTaroAssetId());
        assertEquals("Meta1", addAssetMeatRequest2FromJPA.getMeta());

        // See what's in database with JDBC.
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        Long request1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS", Long.class);
//        assertEquals(request1ID, request1IDFromJDBC);
//        Long addAssetRequest1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS_ADD_ASSET", Long.class);
//        assertEquals(request1ID, addAssetRequest1IDFromJDBC);

    }

}
