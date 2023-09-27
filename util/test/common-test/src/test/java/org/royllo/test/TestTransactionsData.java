package org.royllo.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.test.mempool.TransactionValue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.test.TestTransactions.BITCOIN_TRANSACTION_3_TXID;

@DisplayName("Test asset tests")
public class TestTransactionsData {

    @Test
    @DisplayName("findTransactionByTransactionId()")
    public void findTransactionByTransactionId() {
        // transaction that does not exist.
        assertNull(TestTransactions.findTransactionByTransactionId("TRANSACTION_THAT_DOES_NOT_EXISTS"));

        // =============================================================================================================
        // BITCOIN_TRANSACTION_3_TXID.
        final TransactionValue transaction = TestTransactions.findTransactionByTransactionId((BITCOIN_TRANSACTION_3_TXID));
        assertNotNull(transaction);
        assertEquals(BITCOIN_TRANSACTION_3_TXID, transaction.getTxId());
        assertEquals(3, transaction.getResponse().getVout().size());
    }

}
