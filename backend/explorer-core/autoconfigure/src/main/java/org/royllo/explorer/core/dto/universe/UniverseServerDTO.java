package org.royllo.explorer.core.dto.universe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.util.validator.ServerAddress;

import java.time.ZonedDateTime;

import static lombok.AccessLevel.PRIVATE;

/**
 * Universe server.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class UniverseServerDTO {

    /** Unique identifier. */
    Long id;

    /** Universe server UUID. */
    String universeServerId;

    /** Universe server owner. */
    User owner;

    /** The universe server address (consists of both an IP address and a port number). */
    @ServerAddress(message = "Server address is invalid")
    String serverAddress;

    /** When is the last time we try to synchronize with the server. */
    ZonedDateTime lastSynchronizationAttempt;

    /** When is the last time we successfully synchronized with the server. */
    ZonedDateTime lastSynchronizationSuccess;

}
