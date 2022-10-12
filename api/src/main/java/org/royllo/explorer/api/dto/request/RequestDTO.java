package org.royllo.explorer.api.dto.request;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.api.dto.user.UserDTO;
import org.royllo.explorer.api.util.enums.RequestStatus;

/**
 * A request to update royllo data made by a user.
 */
@Getter
@SuperBuilder
@SuppressWarnings("checkstyle:VisibilityModifier")
public class RequestDTO {

    /** Unique identifier. */
    Long id;

    /** Request creator. */
    UserDTO creator;

    /** Request status. */
    RequestStatus status;

    /** Error message - Not empty if status is equals to ERROR. */
    String errorMessage;

}
