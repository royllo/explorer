package org.royllo.explorer.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.dto.universe.UniverseServerDTO;

/**
 * Request to add a universe server to royllo database.
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddUniverseServerRequestDTO extends RequestDTO {

    /** The universe server address (consists of both an IP address and a port number). */
    @NotBlank(message = "{validation.request.serverAddress.required}")
    private String serverAddress;

    /** The universe server created/updated by this request. */
    private UniverseServerDTO universeServer;

    /**
     * Set the universe server created/updated by this request (Cannot be used to update the universe server).
     *
     * @param newUniverseServer new universe server
     */
    public void setUniverseServer(final UniverseServerDTO newUniverseServer) {
        if (universeServer != null) {
            throw new IllegalStateException("You can't update the target universe server, it's already set");
        }
        universeServer = newUniverseServer;
    }

}
