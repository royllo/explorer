package org.royllo.explorer.core.util.parameters;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Tarod parameters.
 */
@Validated
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "tarod")
public class TarodParameters {

    /** API. */
    @Valid
    private Api api = new Api();

    /** API parameters. */
    @Validated
    @Getter
    @Setter
    @ToString
    @ConfigurationProperties(prefix = "tarod.api")
    public static class Api {

        /** Tarod API base url. */
        @NotEmpty(message = "Tarod API base URL is required")
        private String baseUrl;

        /** Tarod macaroon. */
        @NotEmpty(message = "Macaroon is required")
        private String macaroon;

    }

}
