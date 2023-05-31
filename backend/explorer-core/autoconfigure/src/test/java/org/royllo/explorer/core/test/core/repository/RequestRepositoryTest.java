package org.royllo.explorer.core.test.core.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.domain.request.AddAssetMetaDataRequest;
import org.royllo.explorer.core.domain.request.AddProof;
import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.test.util.BaseTest;
import org.royllo.explorer.core.util.constants.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.RECOVERABLE_FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;

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
        request1.setStatus(OPENED);
        request1.setRawProof("Proof1");
        long request1ID = requestRepository.save(request1).getId();

        // See what's in database with JPA.
        Optional<Request> request1FromJPA = requestRepository.findById(request1ID);
        assertTrue(request1FromJPA.isPresent());
        AddProof addProof1FromJPA = (AddProof) request1FromJPA.get();
        assertEquals(request1ID, addProof1FromJPA.getId());
        Assertions.assertEquals("anonymous", addProof1FromJPA.getCreator().getUsername());
        Assertions.assertEquals(OPENED, addProof1FromJPA.getStatus());
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
        request2.setStatus(SUCCESS);
        request2.setAssetId("TaprootAssetId1");
        request2.setMetaData("Meta1");
        long request2ID = requestRepository.save(request2).getId();

        // See what's in database with JPA.
        Optional<Request> request2FromJPA = requestRepository.findById(request2ID);
        assertTrue(request2FromJPA.isPresent());
        AddAssetMetaDataRequest addAssetMeatRequest2FromJPA = (AddAssetMetaDataRequest) request2FromJPA.get();
        assertEquals(request2ID, addAssetMeatRequest2FromJPA.getId());
        Assertions.assertEquals("anonymous", addAssetMeatRequest2FromJPA.getCreator().getUsername());
        Assertions.assertEquals(SUCCESS, addAssetMeatRequest2FromJPA.getStatus());
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
        request3.setStatus(FAILURE);
        request3.setRawProof("Proof2");
        long request3ID = requestRepository.save(request3).getId();

        // See what's in database with JPA.
        Optional<Request> request3FromJPA = requestRepository.findById(request3ID);
        assertTrue(request3FromJPA.isPresent());
        AddProof addProof2FromJPA = (AddProof) request3FromJPA.get();
        assertEquals(request3ID, addProof2FromJPA.getId());
        Assertions.assertEquals("anonymous", addProof2FromJPA.getCreator().getUsername());
        Assertions.assertEquals(FAILURE, addProof2FromJPA.getStatus());
        assertNull(addProof2FromJPA.getErrorMessage());
        assertEquals("Proof2", addProof2FromJPA.getRawProof());

        // See what's in database with JDBC.
        jdbcTemplate = new JdbcTemplate(dataSource);
        Long request3IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS", Long.class);
        assertEquals(request3ID, request3IDFromJDBC);
        Long addAssetRequest3IDFromJDBC = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM REQUESTS_ADD_PROOF", Long.class);
        assertEquals(request3ID, addAssetRequest3IDFromJDBC);
    }

    @Test
    @Order(1)
    @DisplayName("findByStatusInOrderById")
    public void findByStatusInOrderById() throws SQLException {
        // We start by erasing everything.
        requestRepository.deleteAll();

        // We create 100 "OPENED" requests created before current date minus one month.
        for (int i = 1; i <= 99; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-BEFORE-OPENED-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(OPENED);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "OPENED" requests created after current date minus one month.
        for (int i = 100; i <= 199; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-AFTER-OPENED-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(OPENED);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create 100 "SUCCESS" requests created before one month ago.
        for (int i = 200; i <= 299; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-BEFORE-SUCCESS-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(SUCCESS);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "SUCCESS" requests created after current date minus one month.
        for (int i = 300; i <= 399; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-AFTER-SUCCESS-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(SUCCESS);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create 100 "RECOVERABLE_FAILURE" requests created before one month ago.
        for (int i = 400; i <= 499; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-BEFORE-RECOVERABLE_FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(RECOVERABLE_FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "RECOVERABLE_FAILURE" requests created after current date minus one month.
        for (int i = 500; i <= 599; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-AFTER-RECOVERABLE_FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(RECOVERABLE_FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create 100 "FAILURE" requests created before one month ago.
        for (int i = 600; i <= 699; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-BEFORE-FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "FAILURE" requests created after current date minus one month.
        for (int i = 700; i <= 799; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID() + "-AFTER-FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We should have 799 requests.
        assertEquals(799, requestRepository.count());

        // We retrieve all the failed, there should be 200.
        final Page<Request> results = requestRepository.findByStatusInOrderById(Collections.singletonList(FAILURE), Pageable.ofSize(10));
        assertEquals(200, results.getTotalElements());      // 200 requests.
        assertEquals(20, results.getTotalPages());          // 20 pages of 10 elements.
        assertEquals(10, results.getNumberOfElements());    // 10 elements in the page.
    }

    @Test
    @Order(3)
    @DisplayName("deleteByStatusIsAndCreatedOnBefore()")
    public void deleteByStatusIsAndCreatedOnBefore() throws SQLException {
        // We start by erasing everything.
        requestRepository.deleteAll();

        // Insert request SQL.
        final String updateDate = "UPDATE REQUESTS SET CREATED_ON = ? WHERE REQUEST_ID like ?;";

        // We create 100 "OPENED" requests created before current date minus one month.
        for (int i = 1; i <= 99; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID().toString() + "-BEFORE-OPENED-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(OPENED);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "OPENED" requests created after current date minus one month.
        for (int i = 100; i <= 199; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID().toString() + "-AFTER-OPENED-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(OPENED);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create 100 "SUCCESS" requests created before one month ago.
        for (int i = 200; i <= 299; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID().toString() + "-BEFORE-SUCCESS-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(SUCCESS);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "SUCCESS" requests created after current date minus one month.
        for (int i = 300; i <= 399; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID().toString() + "-AFTER-SUCCESS-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(SUCCESS);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create 100 "RECOVERABLE_FAILURE" requests created before one month ago.
        for (int i = 400; i <= 499; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID().toString() + "-BEFORE-RECOVERABLE_FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(RECOVERABLE_FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "RECOVERABLE_FAILURE" requests created after current date minus one month.
        for (int i = 500; i <= 599; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID().toString() + "-AFTER-RECOVERABLE_FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(RECOVERABLE_FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We create 100 "FAILURE" requests created before one month ago.
        for (int i = 600; i <= 699; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID().toString() + "-BEFORE-FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }
        // We create 100 "FAILURE" requests created after current date minus one month.
        for (int i = 700; i <= 799; i++) {
            AddProof request = new AddProof();
            request.setRequestId(UUID.randomUUID().toString() + "-AFTER-FAILURE-" + String.format("%03d", i));
            request.setCreator(ANONYMOUS_USER);
            request.setStatus(FAILURE);
            request.setRawProof("Proof");
            requestRepository.save(request);
        }

        // We should have 799 requests.
        assertEquals(799, requestRepository.count());

        // We update the creation date to make our test.
        PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(updateDate);
        preparedStatement.setTimestamp(1, Timestamp.from(ZonedDateTime.now().minusMonths(3).toInstant()));
        preparedStatement.setString(2, "%-BEFORE-%");
        preparedStatement.execute();
        preparedStatement = dataSource.getConnection().prepareStatement(updateDate);
        preparedStatement.setTimestamp(1, Timestamp.from(ZonedDateTime.now().minusDays(3).toInstant()));
        preparedStatement.setString(2, "%-AFTER-%");
        preparedStatement.execute();

        // We try to delete all the failed requests older than one month.
        long deletedCount = requestRepository.deleteByStatusIsAndCreatedOnBefore(FAILURE, ZonedDateTime.now().minusMonths(1));
        assertEquals(100, deletedCount);

        // We try to delete all the failed requests older than one month.
        // That is the 100 "FAILURE" requests created before one month ago (600 to 699).
        // So we should go back to 699.
        // And we should not have any "BEFORE-FAILURE-6" request.
        assertEquals(699, requestRepository.count());
        final Optional<Request> notDeletedRequest = requestRepository.findAll()
                .stream()
                .filter(request -> request.getRequestId().contains("BEFORE-FAILURE-6"))
                .findFirst();
        assertFalse(notDeletedRequest.isPresent());
    }

}
