package org.royllo.explorer.core.repository.bitcoin;

import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Bitcoin transaction output repository.
 */
@Repository
public interface BitcoinTransactionOutputRepository extends JpaRepository<BitcoinTransactionOutput, Long> {

    /**
     * Find a transaction out by its txid and index.
     *
     * @param txId transaction id
     * @param vout output index (starts at 0)
     * @return Bitcoin transaction output
     */
    Optional<BitcoinTransactionOutput> findByTxIdAndVout(String txId, int vout);

    /**
     * Find transaction outputs for the corresponding txid.
     *
     * @param txId transaction id
     * @return Bitcoin transaction output
     */
    List<BitcoinTransactionOutput> findByTxId(String txId);

}
