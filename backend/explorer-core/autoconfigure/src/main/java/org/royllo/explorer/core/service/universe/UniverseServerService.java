package org.royllo.explorer.core.service.universe;

import org.royllo.explorer.core.dto.universe.UniverseServerDTO;

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
    UniverseServerDTO addUniverseServer(String serverAddress);


}
