package org.royllo.explorer.core.repository.universe;

import org.royllo.explorer.core.domain.universe.UniverseServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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
     * @return server address
     */
    Optional<UniverseServer> findByServerAddress(String serverAddress);


    /**
     * Find the top 3 servers that have never been synchronized.
     *
     * @return top 3 servers to synchronize
     */
    List<UniverseServer> findTop3ByOrderByLastSynchronizedOnAsc();

}
