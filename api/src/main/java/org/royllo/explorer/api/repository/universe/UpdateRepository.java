package org.royllo.explorer.api.repository.universe;

import org.royllo.explorer.api.domain.universe.Update;
import org.royllo.explorer.api.util.enums.UpdateRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Update} repository.
 */
@Repository
public interface UpdateRepository extends JpaRepository<Update, Long> {

    /**
     * Find all updates with the corresponding status.
     *
     * @param status status filter
     * @return updates with the corresponding status
     */
    List<Update> findByStatus(UpdateRequestStatus status);

    /**
     * Find all updates not having the status passed as parameter.
     *
     * @param status status to exclude
     * @return updates
     */
    List<Update> findByStatusNotIn(List<UpdateRequestStatus> status);

}
