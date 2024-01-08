package org.royllo.explorer.web.util.interceptor;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.bucket4j.BandwidthBuilder;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.royllo.explorer.core.util.parameters.IncomingRateLimitsParameters;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Rate limit interceptor.
 */
public final class RateLimitInterceptor implements HandlerInterceptor {

    /** Cache is a kind of map storing and IP address with its corresponding bucket. */
    private static LoadingCache<String, Bucket> cache;

    /**
     * Default constructor - Builds the cache.
     *
     * @param incomingRateLimitsParameters Incoming rate parameters.
     */
    public RateLimitInterceptor(final IncomingRateLimitsParameters incomingRateLimitsParameters) {
        cache = Caffeine.newBuilder()
                .maximumSize(incomingRateLimitsParameters.getCache().getMaximumSize())
                .expireAfterWrite(incomingRateLimitsParameters.getCache().getExpireAfterWrite())
                .build(key -> Bucket.builder()
                        .addLimit(BandwidthBuilder.builder()
                                .capacity(incomingRateLimitsParameters.getBandwidth().getCapacity())
                                .refillIntervally(1, incomingRateLimitsParameters.getBandwidth().getRefillPeriod()).build())
                        .build());
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public boolean preHandle(final HttpServletRequest request,
                             @NotNull final HttpServletResponse response,
                             @NotNull final Object handler) throws Exception {
        // We check in the cache if the IP address is already present with a bucket.
        ConsumptionProbe probe = cache.get(request.getRemoteAddr()).tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            // If we can consume from the bucket, we add a header to indicate how many are left.
            response.addHeader("X-Rate-Limit-Remaining", Long.toString(probe.getRemainingTokens()));
            return true;
        } else {
            // If we cannot consume from the bucket, we return an error.
            response.addHeader("X-Rate-Limit-Retry-After-Milliseconds", Long.toString(probe.getNanosToWaitForRefill() / 1_000_000));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "You have exhausted your API Request Quota");
            return false;
        }
    }

}
