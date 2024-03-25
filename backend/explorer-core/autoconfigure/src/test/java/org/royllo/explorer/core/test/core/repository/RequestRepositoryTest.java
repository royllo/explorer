package org.royllo.explorer.core.test.core.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.domain.request.AddProofRequest;
import org.royllo.explorer.core.domain.request.AddUniverseServerRequest;
import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;

@SpringBootTest
@DirtiesContext
@DisplayName("RequestRepository tests")
public class RequestRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    @DisplayName("Request inheritance")
    public void requestInheritance() {
        // This test is used to validate how inheritance with JPA works.

        // =============================================================================================================
        // Creating request 1 (ADD_ASSET type).
        AddProofRequest request1 = new AddProofRequest();
        request1.setRequestId(UUID.randomUUID().toString());
        request1.setCreator(ANONYMOUS_USER);
        request1.setStatus(OPENED);
        request1.setProof("Proof1");
        long request1ID = requestRepository.save(request1).getId();

        // See what's in database with JPA.
        Optional<Request> request1FromJPA = requestRepository.findById(request1ID);
        assertTrue(request1FromJPA.isPresent());
        AddProofRequest addProofRequest1FromJPA = (AddProofRequest) request1FromJPA.get();
        assertEquals(request1ID, addProofRequest1FromJPA.getId());
        assertEquals(ANONYMOUS_USER_USERNAME, addProofRequest1FromJPA.getCreator().getUsername());
        assertEquals(OPENED, addProofRequest1FromJPA.getStatus());
        assertNull(addProofRequest1FromJPA.getErrorMessage());
        assertEquals("Proof1", addProofRequest1FromJPA.getProof());

        // See what's in database with JDBC.
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Long request1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST", Long.class);
        assertEquals(request1ID, request1IDFromJDBC);
        Long addAssetRequest1IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST_ADD_PROOF", Long.class);
        assertEquals(request1ID, addAssetRequest1IDFromJDBC);

        // =============================================================================================================
        // Creating request 2 (ADD_UNIVERSE_SERVER type).
        AddUniverseServerRequest request2 = new AddUniverseServerRequest();
        request2.setRequestId(UUID.randomUUID().toString());
        request2.setCreator(ANONYMOUS_USER);
        request2.setStatus(SUCCESS);
        request2.setServerAddress("serverAddress1");
        long request2ID = requestRepository.save(request2).getId();

        // See what's in database with JPA.
        Optional<Request> request2FromJPA = requestRepository.findById(request2ID);
        assertTrue(request2FromJPA.isPresent());
        AddUniverseServerRequest addUniverseServerRequest2FromJPA = (AddUniverseServerRequest) request2FromJPA.get();
        assertEquals(request2ID, addUniverseServerRequest2FromJPA.getId());
        assertEquals(ANONYMOUS_USER_USERNAME, addUniverseServerRequest2FromJPA.getCreator().getUsername());
        assertEquals(SUCCESS, addUniverseServerRequest2FromJPA.getStatus());
        assertNull(addUniverseServerRequest2FromJPA.getErrorMessage());
        assertEquals("serverAddress1", addUniverseServerRequest2FromJPA.getServerAddress());

        // See what's in database with JDBC.
        jdbcTemplate = new JdbcTemplate(dataSource);
        Long request2IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST", Long.class);
        assertEquals(request2ID, request2IDFromJDBC);
        Long addAssetRequest2IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST_ADD_UNIVERSE_SERVER", Long.class);
        assertEquals(request2ID, addAssetRequest2IDFromJDBC);

        // =============================================================================================================
        // Creating request 3 (ADD_ASSET type).
        AddProofRequest request3 = new AddProofRequest();
        request3.setRequestId(UUID.randomUUID().toString());
        request3.setCreator(ANONYMOUS_USER);
        request3.setStatus(FAILURE);
        request3.setProof("Proof2");
        long request3ID = requestRepository.save(request3).getId();

        // See what's in database with JPA.
        Optional<Request> request3FromJPA = requestRepository.findById(request3ID);
        assertTrue(request3FromJPA.isPresent());
        AddProofRequest addProofRequest2FromJPA = (AddProofRequest) request3FromJPA.get();
        assertEquals(request3ID, addProofRequest2FromJPA.getId());
        assertEquals(ANONYMOUS_USER_USERNAME, addProofRequest2FromJPA.getCreator().getUsername());
        assertEquals(FAILURE, addProofRequest2FromJPA.getStatus());
        assertNull(addProofRequest2FromJPA.getErrorMessage());
        assertEquals("Proof2", addProofRequest2FromJPA.getProof());

        // See what's in database with JDBC.
        jdbcTemplate = new JdbcTemplate(dataSource);
        Long request3IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST", Long.class);
        assertEquals(request3ID, request3IDFromJDBC);
        Long addAssetRequest3IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUEST_ADD_PROOF", Long.class);
        assertEquals(request3ID, addAssetRequest3IDFromJDBC);
    }

}
