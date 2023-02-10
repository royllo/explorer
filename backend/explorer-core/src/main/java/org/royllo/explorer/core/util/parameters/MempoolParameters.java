package org.royllo.explorer.core.util.parameters;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

/**
 * Mempool parameters.
 */
@Validated
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "mempool")
public class MempoolParameters {

    /** API. */
    @Valid
    private Api api = new Api();

    /** Exchange modes. */
    @Validated
    @Getter
    @Setter
    @ToString
    @ConfigurationProperties(prefix = "mempool.api")
    public static class Api {

        /** Mempool API base url. */
        @NotEmpty(message = "Mempool API base URL is required")
        private String baseUrl;

    }

}
