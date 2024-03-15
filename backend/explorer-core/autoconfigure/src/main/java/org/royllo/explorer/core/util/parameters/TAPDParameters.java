package org.royllo.explorer.core.util.parameters;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * TAPD parameters.
 */
@Validated
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "tapd")
public class TAPDParameters {

    /** API. */
    @Valid
    private Api api = new Api();

    /** API parameters. */
    @Validated
    @Getter
    @Setter
    @ToString
    @ConfigurationProperties(prefix = "tapd.api")
    public static class Api {

        /** TAPD API base url. */
        @NotEmpty(message = "TAPD API base URL is required")
        private String baseUrl;

        /** TAPD macaroon. */
        @NotEmpty(message = "TAPD Macaroon is required")
        private String macaroon;

    }

}
