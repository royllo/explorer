package org.royllo.explorer.core.configuration;

import org.royllo.explorer.core.util.parameters.IncomingRateLimitsParameters;
import org.royllo.explorer.core.util.parameters.MempoolParameters;
import org.royllo.explorer.core.util.parameters.OutgoingRateLimitsParameters;
import org.royllo.explorer.core.util.parameters.RoylloExplorerParameters;
import org.royllo.explorer.core.util.parameters.TAPDParameters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Parameters configuration.
 */
@Configuration
@EnableConfigurationProperties({
        // Royllo explorer configuration.
        RoylloExplorerParameters.class,
        RoylloExplorerParameters.Api.class,
        RoylloExplorerParameters.Web.class,
        // Mempool configuration.
        MempoolParameters.class,
        MempoolParameters.Api.class,
        // TAPD configuration.
        TAPDParameters.class,
        TAPDParameters.Api.class,
        // Rate limit configuration.
        IncomingRateLimitsParameters.class,
        IncomingRateLimitsParameters.Cache.class,
        IncomingRateLimitsParameters.Bandwidth.class,
        OutgoingRateLimitsParameters.class
})
public class ParametersConfiguration {
}
