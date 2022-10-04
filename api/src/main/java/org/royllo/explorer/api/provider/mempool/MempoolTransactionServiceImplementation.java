package org.royllo.explorer.api.provider.mempool;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.royllo.explorer.api.util.base.BaseMempoolService;
import org.royllo.explorer.api.util.parameters.MempoolParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Mempool transaction service implementation.
 */
@Service
@RequiredArgsConstructor
public class MempoolTransactionServiceImplementation extends BaseMempoolService implements MempoolTransactionService {

    /** Mempool parameters. */
    private final MempoolParameters mempoolParameters;

    @Override
    @SuppressWarnings("checkstyle:DesignForExtension")
    public Mono<GetTransactionResponse> getTransaction(final String txid) {
        logger.debug("Calling mempool for transaction {}", txid);
        return WebClient.create(mempoolParameters.getApi().getBaseUrl())
                .get()
                .uri("/tx/" + txid)
                .retrieve()
                .bodyToMono(GetTransactionResponse.class)
                .retryWhen(defaultRetryConfiguration)
                .doOnError(throwable -> logger.error("Error calling mempool for transaction {}: {}",
                        txid,
                        ExceptionUtils.getRootCause(throwable).getMessage()))
                .onErrorResume(throwable -> Mono.empty());
    }

}
