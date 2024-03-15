package org.royllo.explorer.core.repository.bitcoin;

import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link BitcoinTransactionOutput} repository.
 */
@Repository
@SuppressWarnings("unused")
public interface BitcoinTransactionOutputRepository extends JpaRepository<BitcoinTransactionOutput, Long> {

    /**
     * Find all transaction outputs for a bitcoin transaction id.
     *
     * @param txId bitcoin transaction id
     * @return Bitcoin transaction outputs
     */
    List<BitcoinTransactionOutput> findByTxId(String txId);

}
