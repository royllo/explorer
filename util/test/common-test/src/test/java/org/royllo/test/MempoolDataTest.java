package org.royllo.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.test.mempool.TransactionValue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;

@DisplayName("Test mempool data")
public class MempoolDataTest {

    @Test
    @DisplayName("findTransactionByTransactionId()")
    public void findTransactionByTransactionId() {
        // =============================================================================================================
        // transaction that does not exist.
        assertNull(MempoolData.findTransactionByTransactionId("TRANSACTION_THAT_DOES_NOT_EXISTS"));

        // =============================================================================================================
        // ROYLLO_COIN_GENESIS_TXID.
        final TransactionValue transaction = MempoolData.findTransactionByTransactionId(ROYLLO_COIN_GENESIS_TXID);
        assertNotNull(transaction);
        assertEquals(ROYLLO_COIN_GENESIS_TXID, transaction.getTxId());
        assertEquals(2, transaction.getResponse().getVout().size());
    }

}
