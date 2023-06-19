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
     * Returns the list of status that are finals - Updates with these status cannot be changed.
     *
     * @return list of final status
     */
    public static List<RequestStatus> openedStatus() {
        return List.of(OPENED);
    }

    /**
     * Returns the list of status that are finals - Updates with these status cannot be changed.
     *
     * @return list of final status
     */
    public static List<RequestStatus> finalStatus() {
        return List.of(SUCCESS, FAILURE);
    }

}
