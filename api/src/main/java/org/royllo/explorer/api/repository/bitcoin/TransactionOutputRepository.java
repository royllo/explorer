package org.royllo.explorer.api.repository.bitcoin;

import org.royllo.explorer.api.domain.bitcoin.TransactionOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Bitcoin transaction output repository.
 */
@Repository
public interface TransactionOutputRepository extends JpaRepository<TransactionOutput, Long> {

    /**
     * Find a transaction out by its txid and index.
     *
     * @param txId transaction id
     * @param vout output index (starts at 0)
     * @return Bitcoin transaction output
     */
    Optional<TransactionOutput> findByTxIdAndVout(String txId, int vout);

    /**
     * Find transaction outputs for the corresponding txid.
     *
     * @param txId transaction id
     * @return Bitcoin transaction output
     */
    List<TransactionOutput> findByTxId(String txId);

}
