package org.royllo.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.test.mempool.GetTransactionValueResponse;
import org.royllo.test.mempool.TransactionValue;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.test.MempoolData.ROYLLO_COIN_ANCHOR_1_TXID;
import static org.royllo.test.MempoolData.ROYLLO_COIN_ANCHOR_1_VOUT;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_VOUT;

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
        assertEquals(25, transaction.getResponse().getVout().size());
    }

    @Test
    @DisplayName("Royllo coin transaction value")
    public void roylloCoinTransactionValue() {
        // Genesis txid.
        final TransactionValue genesisTransaction = MempoolData.findTransactionByTransactionId(ROYLLO_COIN_GENESIS_TXID);
        assertNotNull(genesisTransaction);
        assertEquals(ROYLLO_COIN_GENESIS_TXID, genesisTransaction.getResponse().getTxId());
        assertEquals(816513, genesisTransaction.getResponse().getStatus().getBlockHeight());
        assertEquals(25, genesisTransaction.getResponse().getVout().size());

        // Genesis vOut.
        final GetTransactionValueResponse.VOut genesisVOut = genesisTransaction.getResponse().getVout().get(ROYLLO_COIN_GENESIS_VOUT);
        assertEquals("001472ce14cdf4c24b53e7c45d1bd1eaa904114de962", genesisVOut.getScriptPubKey());
        assertEquals("OP_0 OP_PUSHBYTES_20 72ce14cdf4c24b53e7c45d1bd1eaa904114de962", genesisVOut.getScriptPubKeyAsm());
        assertEquals("v0_p2wpkh", genesisVOut.getScriptPubKeyType());
        assertEquals("bc1qwt8pfn05cf948e7yt5dar64fqsg5m6tz8835gs", genesisVOut.getScriptPubKeyAddress());
        assertEquals(0, BigInteger.valueOf(287379).compareTo(genesisVOut.getValue()));
        assertEquals(1699822321, genesisTransaction.getResponse().getStatus().getBlockTime());

        // Anchor 1 txid.
        final TransactionValue anchor1Transaction = MempoolData.findTransactionByTransactionId(ROYLLO_COIN_ANCHOR_1_TXID);
        assertNotNull(anchor1Transaction);
        assertEquals(ROYLLO_COIN_ANCHOR_1_TXID, anchor1Transaction.getResponse().getTxId());
        assertEquals(816610, anchor1Transaction.getResponse().getStatus().getBlockHeight());
        assertEquals(2, anchor1Transaction.getResponse().getVout().size());

        // Anchor 1 vOut.
        final GetTransactionValueResponse.VOut anchor1VOut = anchor1Transaction.getResponse().getVout().get(ROYLLO_COIN_ANCHOR_1_VOUT);
        assertEquals("51200e7b1c167645f8fea7d7d52d9fd2655822d53e9f56e7ce5261635955d18906f1", anchor1VOut.getScriptPubKey());
        assertEquals("OP_PUSHNUM_1 OP_PUSHBYTES_32 0e7b1c167645f8fea7d7d52d9fd2655822d53e9f56e7ce5261635955d18906f1", anchor1VOut.getScriptPubKeyAsm());
        assertEquals("v1_p2tr", anchor1VOut.getScriptPubKeyType());
        assertEquals("bc1ppea3c9nkghu0af7h65kel5n9tq3d205l2mnuu5npvdv4t5vfqmcsh2ttkn", anchor1VOut.getScriptPubKeyAddress());
        assertEquals(0, BigInteger.valueOf(1000).compareTo(anchor1VOut.getValue()));
        assertEquals(1699886669, anchor1Transaction.getResponse().getStatus().getBlockTime());
    }

}
