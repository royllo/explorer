package org.royllo.explorer.core.util.base;

import reactor.util.retry.Retry;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * Base mempool service.
 */
public abstract class BaseMempoolService extends Base {

    /** Max attempts. */
    private static final int RETRY_MAXIMUM_ATTEMPTS = 3;

    /** Duration between retries. */
    private static final Duration RETRY_DURATION_BETWEEN_ATTEMPTS = Duration.of(6, SECONDS);

    /** Default service call retry configuration. */
    protected final Retry defaultRetryConfiguration = Retry.backoff(RETRY_MAXIMUM_ATTEMPTS, RETRY_DURATION_BETWEEN_ATTEMPTS);

}
