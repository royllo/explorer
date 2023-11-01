package org.royllo.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.test.mempool.GetTransactionValueResponse;
import org.royllo.test.mempool.TransactionValue;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;

@DisplayName("Test transactions data")
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

    @Test
    @DisplayName("Royllo coin transaction value")
    public void roylloCoinTransactionValue() {
        final TransactionValue transaction = MempoolData.findTransactionByTransactionId(ROYLLO_COIN_GENESIS_TXID);
        assertNotNull(transaction);
        assertEquals(ROYLLO_COIN_GENESIS_TXID, transaction.getResponse().getTxId());
        assertEquals(2534138, transaction.getResponse().getStatus().getBlockHeight());
        assertEquals(2, transaction.getResponse().getVout().size());

        // VOut 0.
        final GetTransactionValueResponse.VOut vOut0 = transaction.getResponse().getVout().get(0);
        assertEquals("00142e42579b5194674680049c65249edb0893aa2d6b", vOut0.getScriptPubKey());
        assertEquals("OP_0 OP_PUSHBYTES_20 2e42579b5194674680049c65249edb0893aa2d6b", vOut0.getScriptPubKeyAsm());
        assertEquals("v0_p2wpkh", vOut0.getScriptPubKeyType());
        assertEquals("tb1q9ep90x63j3n5dqqyn3jjf8kmpzf65tttupudyg", vOut0.getScriptPubKeyAddress());
        assertEquals(0, BigInteger.valueOf(1100379).compareTo(vOut0.getValue()));

        // VOut 1.
        final GetTransactionValueResponse.VOut vOut1 = transaction.getResponse().getVout().get(1);
        assertEquals("00149254ff3e9788825ebe42dd2cc251fead1d8b2938", vOut1.getScriptPubKey());
        assertEquals("OP_0 OP_PUSHBYTES_20 9254ff3e9788825ebe42dd2cc251fead1d8b2938", vOut1.getScriptPubKeyAsm());
        assertEquals("v0_p2wpkh", vOut1.getScriptPubKeyType());
        assertEquals("tb1qjf20705h3zp9a0jzm5kvy50745wck2fcp5zq02", vOut1.getScriptPubKeyAddress());
        assertEquals(0, BigInteger.valueOf(1239334904).compareTo(vOut1.getValue()));
    }

}
