package org.royllo.explorer.core.util.enums;

import java.util.List;

/**
 * Request status.
 */
public enum RequestStatus {

    /** An opened request that must be treated. */
    OPENED,

    /** The request has been treated with success. */
    SUCCESS,

    /** An error occurred, it's a failure. */
    FAILURE;

    /**
     * Returns the list of opened status (Work to do).
     *
     * @return list of opened status
     */
    public static List<RequestStatus> openedStatus() {
        return List.of(OPENED);
    }

}
