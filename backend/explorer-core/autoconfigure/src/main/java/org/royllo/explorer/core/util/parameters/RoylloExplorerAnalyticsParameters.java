package org.royllo.explorer.core.util.parameters;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Royllo explorer analytics parameters.
 */
@Validated
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "royllo.explorer.analytics")
public class RoylloExplorerAnalyticsParameters {

    /** Piwik analytics. */
    private Piwik piwik = new Piwik();

    /** Piwik parameters. */
    @Validated
    @Getter
    @Setter
    @ToString
    @ConfigurationProperties(prefix = "piwik")
    public static class Piwik {

        /** Piwik tracking id. */
        private String trackingId;

    }

}
