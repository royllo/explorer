package org.royllo.explorer.api.graphql.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * createAddUniverseServerRequest inputs.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AddUniverseServerRequestInputs {

    /** The server address (consists of both an IP address and a port number). */
    private String serverAddress;

}
