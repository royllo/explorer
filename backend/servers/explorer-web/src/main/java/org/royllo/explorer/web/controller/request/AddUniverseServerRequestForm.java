package org.royllo.explorer.web.controller.request;

import lombok.Data;
import org.royllo.explorer.core.util.validator.ServerAddress;

/**
 * Add universe server request form.
 */
@Data
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddUniverseServerRequestForm {

    /**
     * The server address typically consists of both an IP address and a port number.
     */
    @ServerAddress(message = "{request.universeServer.serverAddress.error.invalidServerAddress}")
    private String serverAddress;

}
