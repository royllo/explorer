package org.royllo.explorer.core.repository.bitcoin;

import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link BitcoinTransactionOutput} repository.
 */
@Repository
public interface BitcoinTransactionOutputRepository extends JpaRepository<BitcoinTransactionOutput, Long> {

    /**
     * Find transaction outputs for the corresponding txid.
     *
     * @param txId transaction id
     * @return Bitcoin transaction outputs
     */
    List<BitcoinTransactionOutput> findByTxId(String txId);

}
