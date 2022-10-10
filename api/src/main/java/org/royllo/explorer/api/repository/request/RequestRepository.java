package org.royllo.explorer.api.repository.request;

import org.royllo.explorer.api.domain.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Request repository.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

}
