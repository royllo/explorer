package org.royllo.explorer.backend.service.bitcoin;

import org.royllo.explorer.backend.dto.bitcoin.TransactionOutputDTO;

import java.util.Optional;

/**
 * Bitcoin service.
 */
public interface BitcoinService {

    /**
     * Returns details about a specific transaction output.
     *
     * @param txId transaction id
     * @param vOut output index (starts at 0)
     * @return transaction details
     */
    Optional<TransactionOutputDTO> getBitcoinTransactionOutput(String txId, int vOut);

}
