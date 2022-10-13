package org.royllo.explorer.api.repository.request;

import org.royllo.explorer.api.domain.request.Request;
import org.royllo.explorer.api.util.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Request repository.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    /**
     * Find all requests with the corresponding status.
     *
     * @param status status filter
     * @return Requests with the corresponding status
     */
    List<Request> findByStatusOrderById(RequestStatus status);

}
