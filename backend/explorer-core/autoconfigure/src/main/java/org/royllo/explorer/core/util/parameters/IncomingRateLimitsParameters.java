package org.royllo.explorer.core.util.parameters;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

/**
 * Incoming rate parameters.
 */
@Validated
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "rate.incoming")
public class IncomingRateLimitsParameters {

    /** Cache. */
    @Valid
    private IncomingRateLimitsParameters.Cache cache = new IncomingRateLimitsParameters.Cache();

    /** Bandwidth. */
    @Valid
    private IncomingRateLimitsParameters.Bandwidth bandwidth = new IncomingRateLimitsParameters.Bandwidth();

    /** Cache parameters. */
    @Validated
    @Getter
    @Setter
    @ToString
    @ConfigurationProperties(prefix = "rate.incoming.cache")
    public static class Cache {

        /** Defines the maximum number of entries the cache may contain. */
        @Positive(message = "Incoming cache maximum size is required")
        private Integer maximumSize;

        /** Defines that each entry should be automatically removed from the cache once a fixed duration has elapsed after the entry's creation, or the most recent replacement of its value. */
        @NotNull(message = "Incoming expire after write is required")
        private Duration expireAfterWrite;

    }

    /** Bandwidth parameters. */
    @Validated
    @Getter
    @Setter
    @ToString
    @ConfigurationProperties(prefix = "rate.incoming.bandwidth")
    public static class Bandwidth {

        /** Defines the maximum count of tokens which can be hold by a bucket. */
        @Positive(message = "Incoming bandwidth capacity is required")
        private Integer capacity;

        /** Defines the speed in which tokens are regenerated in bucket. */
        @NotNull(message = "Incoming bandwidth refill period is required")
        private Duration refillPeriod;

    }

}
