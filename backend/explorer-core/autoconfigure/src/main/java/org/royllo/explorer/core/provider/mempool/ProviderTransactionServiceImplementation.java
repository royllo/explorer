package org.royllo.explorer.core.provider.mempool;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.util.base.BaseProviderService;
import org.royllo.explorer.core.util.parameters.MempoolParameters;
import org.royllo.explorer.core.util.parameters.OutgoingRateLimitsParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Mempool transaction service implementation.
 */
@Service
@RequiredArgsConstructor
public class ProviderTransactionServiceImplementation extends BaseProviderService implements MempoolTransactionService {

    /** Outgoing rate limits parameters. */
    private final OutgoingRateLimitsParameters outgoingRateLimitsParameters;

    /** Mempool parameters. */
    private final MempoolParameters mempoolParameters;

    /**
     * Post construct.
     */
    @PostConstruct
    private void postConstruct() {
        bucket = Bucket.builder()
                .addLimit(Bandwidth.simple(1, outgoingRateLimitsParameters.getDelayBetweenRequests()))
                .build();
    }

    @Override
    @SuppressWarnings("checkstyle:DesignForExtension")
    public Mono<GetTransactionResponse> getTransaction(final String txid) {
        logger.debug("Calling mempool for transaction {}", txid);

        // Consume a token from the token bucket.
        // If a token is not available this method will block until the refill adds one to the bucket.
        try {
            bucket.asBlocking().consume(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return WebClient.create(mempoolParameters.getApi().getBaseUrl())
                .get()
                .uri("/tx/" + txid)
                .retrieve()
                .bodyToMono(GetTransactionResponse.class)
                .retryWhen(defaultRetryConfiguration)
                .doOnError(throwable -> logger.error("Error calling mempool for transaction {}: {}",
                        txid,
                        throwable.getMessage()))
                .onErrorResume(throwable -> Mono.empty());
    }

}
