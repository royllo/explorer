package org.royllo.explorer.core.dto.request;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.util.enums.RequestStatus;

/**
 * A request to update royllo data made by a user.
 */
@Getter
@SuperBuilder
@ToString
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
