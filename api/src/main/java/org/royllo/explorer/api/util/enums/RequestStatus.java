package org.royllo.explorer.api.util.enums;

import java.util.List;

/**
 * Request status.
 */
public enum RequestStatus {

    /** New request: a new request has been added. */
    NEW,

    /** The request has been treated with success. */
    SUCCESS,

    /** An error occurred and the process stopped. */
    ERROR;

    /**
     * Returns the list of status that are finals - Updates with these status cannot be changed.
     *
     * @return list of final status
     */
    public static List<RequestStatus> finalStatus() {
        return List.of(SUCCESS, ERROR);
    }

}
