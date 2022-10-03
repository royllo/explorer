package org.royllo.explorer.backend.util.enums;

import java.util.List;

/**
 * Universe update requests status.
 */
public enum UpdateRequestStatus {

    /** New request: a new request has been added. */
    NEW,

    /** Transaction output checked: transaction output exists, and it's a locked with taproot script. */
    TRANSACTION_OUTPUT_CHECKED,

    /** Taproot script checked. */
    TAPROOT_SCRIPT_CHECKED,

    /** The request has been treated without error. */
    TREATED,

    /** An error occurred and the process stopped. */
    ERROR;

    /**
     * Returns the list of status that are finals - Updates with these status cannot be changed.
     *
     * @return list of final status
     */
    public static List<UpdateRequestStatus> finalStatus() {
        return List.of(TREATED, ERROR);
    }

}
