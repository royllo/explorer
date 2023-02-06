package org.royllo.explorer.core.provider.mempool;

import reactor.core.publisher.Mono;

/**
 * Mempool transaction service.
 * <a href="https://mempool.space/fr/docs/api/rest">Mempool API documentation</a>
 */
public interface MempoolTransactionService {

    /**
     * Returns details about a specific transaction.
     *
     * @param txid transaction id
     * @return transaction details
     */
    Mono<GetTransactionResponse> getTransaction(String txid);

}
