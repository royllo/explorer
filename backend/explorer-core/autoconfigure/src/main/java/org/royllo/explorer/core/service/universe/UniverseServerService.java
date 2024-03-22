package org.royllo.explorer.core.service.universe;

import org.royllo.explorer.core.dto.universe.UniverseServerDTO;
import org.royllo.explorer.core.util.validator.ServerAddress;

import java.util.List;
import java.util.Optional;

/**
 * Universe server service.
 */
public interface UniverseServerService {

    /**
     * Add a new universe server.
     *
     * @param serverAddress server address
     * @return universe server created
     */
    UniverseServerDTO addUniverseServer(@ServerAddress String serverAddress);

    /**
     * Get a universe server by its server address.
     *
     * @param serverAddress server address
     * @return universe server
     */
    Optional<UniverseServerDTO> getUniverseServerByServerAddress(String serverAddress);

    /**
     * Get all universe servers.
     *
     * @return all universe servers.
     */
    List<UniverseServerDTO> getAllUniverseServers();

}
