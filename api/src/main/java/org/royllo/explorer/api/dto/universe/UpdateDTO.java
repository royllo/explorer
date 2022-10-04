package org.royllo.explorer.api.dto.universe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.api.dto.user.UserDTO;
import org.royllo.explorer.api.util.enums.UpdateRequestStatus;

import static lombok.AccessLevel.PRIVATE;

/**
 * Universe update.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class UpdateDTO {

    /** Unique identifier. */
    Long id;

    /** The user who created this universe update. */
    UserDTO creator;

    /** Universe update request status. */
    UpdateRequestStatus status;

    /** Transaction id. */
    String txId;

    /** Output index (starts at 0). */
    int vout;

    /** Indicates in which status the error occurred. */
    UpdateRequestStatus failureStatus;

    /** Failure message. */
    String failureMessage;

}
