package org.royllo.explorer.core.repository.universe;

import org.royllo.explorer.core.domain.universe.UniverseServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link UniverseServer} repository.
 */
@Repository
public interface UniverseServerRepository extends JpaRepository<UniverseServer, Long> {

    /**
     * Find a server by its server address.
     *
     * @param serverAddress server address
     * @return server
     */
    Optional<UniverseServer> findByServerAddress(String serverAddress);


    /**
     * Find the next server to synchronise.
     *
     * @return server to synchronize
     */
    Optional<UniverseServer> findFirstByOrderByLastSynchronizationAttemptAsc();

}
