package org.royllo.explorer.core.repository.util;

import org.royllo.explorer.core.domain.util.K1Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * {@link K1Value} repository.
 */
@Repository
public interface K1ValueRepository extends JpaRepository<K1Value, String> {

    /**
     * Find all K1 values created before a given date.
     *
     * @param createdOn the date
     * @return the list of K1 values
     */
    List<K1Value> findByCreatedOnBefore(ZonedDateTime createdOn);

}
