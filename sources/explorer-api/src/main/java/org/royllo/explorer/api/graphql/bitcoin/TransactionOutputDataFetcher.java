package org.royllo.explorer.api.graphql.bitcoin;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;

import java.util.Optional;

/**
 * Bitcoin transaction output data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class TransactionOutputDataFetcher extends BaseDataFetcher {

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
        final Optional<BitcoinTransactionOutputDTO> bitcoinTransactionOutput = bitcoinService.getBitcoinTransactionOutput(txId, vout);
        if (bitcoinTransactionOutput.isEmpty()) {
            logger.info("bitcoinTransactionOutput - Transaction output with txId:vout {}:{} not found", txId, vout);
            throw new DgsEntityNotFoundException();
        } else {
            logger.info("bitcoinTransactionOutput - Transaction output with txId:vout {}:{} found: {}", txId, vout, bitcoinTransactionOutput.get());
            return bitcoinTransactionOutput.get();
        }
    }

}
