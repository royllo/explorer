package org.royllo.explorer.api.web.graphql.bitcoin;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.api.service.bitcoin.BitcoinService;

/**
 * Bitcoin transaction output data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class TransactionOutputDataFetcher {

    /** Bitcoin service. */
    private final BitcoinService bitcoinService;

    /**
     * Get transaction output by its txId and vout.
     *
     * @param txId transaction id
     * @param vout vout
     * @return transaction output
     */
    @DgsQuery
    public final BitcoinTransactionOutputDTO bitcoinTransactionOutput(final @InputArgument String txId,
                                                                      final @InputArgument int vout) {
        return bitcoinService.getBitcoinTransactionOutput(txId, vout).orElse(null);
    }

}
