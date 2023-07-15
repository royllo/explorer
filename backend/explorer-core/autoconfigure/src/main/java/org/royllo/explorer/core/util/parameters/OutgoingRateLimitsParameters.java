package org.royllo.explorer.core.util.parameters;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

/**
 * Outgoing rate parameters.
 */
@Validated
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "rate.outgoing")
public class OutgoingRateLimitsParameters {

    /** Delay between outgoing requests. */
    @NotNull(message = "Delay between outgoing requests is required")
    private Duration delayBetweenRequests;

}
