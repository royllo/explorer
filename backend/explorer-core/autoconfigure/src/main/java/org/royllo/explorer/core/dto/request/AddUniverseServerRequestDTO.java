package org.royllo.explorer.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Request to add a royllo universe server to royllo database.
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddUniverseServerRequestDTO extends RequestDTO {

    /** The universe server address (consists of both an IP address and a port number). */
    @NotBlank(message = "Server address is required")
    private String serverAddress;

}
