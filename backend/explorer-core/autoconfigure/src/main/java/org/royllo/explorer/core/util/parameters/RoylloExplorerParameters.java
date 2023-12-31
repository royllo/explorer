package org.royllo.explorer.core.util.parameters;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Royllo explorer parameters.
 */
@Validated
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "royllo.explorer")
public class RoylloExplorerParameters {

    /** API. */
    @Valid
    private Api api = new Api();

    /** API parameters. */
    @Validated
    @Getter
    @Setter
    @ToString
    @ConfigurationProperties(prefix = "api")
    public static class Api {

        /** Royllo explorer API base url. */
        private String baseUrl;

    }

    /** Web. */
    @Valid
    private Web web = new Web();

    /** Web parameters. */
    @Validated
    @Getter
    @Setter
    @ToString
    @ConfigurationProperties(prefix = "web")
    public static class Web {

        /** Royllo explorer web base url. */
        private String baseUrl;

    }

    /** content. */
    @Valid
    private RoylloExplorerParameters.Content content = new Content();

    /** Content parameters. */
    @Validated
    @Getter
    @Setter
    @ToString
    @ConfigurationProperties(prefix = "content")
    public static class Content {

        /** Royllo explorer content base url. */
        private String baseUrl;

    }

}
