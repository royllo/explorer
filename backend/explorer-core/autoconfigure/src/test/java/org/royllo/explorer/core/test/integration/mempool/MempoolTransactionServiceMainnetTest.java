package org.royllo.explorer.core.test.integration.mempool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.mempool.GetTransactionResponse;
import org.royllo.explorer.core.provider.mempool.MempoolTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(properties = {"mempool.api.base-url=https://mempool.space/api"})
@DisplayName("Mempool transaction service on mainnet")
public class MempoolTransactionServiceMainnetTest {

    @Autowired
    private MempoolTransactionService mempoolTransactionService;

    @Test
    @DisplayName("getTransaction() on mainnet")
    @SuppressWarnings("SpellCheckingInspection")
    public void getTransactionTest() {
        // =============================================================================================================
        // Getting a non-existing transaction.
        final GetTransactionResponse nonExistingTransaction = mempoolTransactionService.getTransaction("NON_EXISTING_TRANSACTION_ID").block();
        assertNull(nonExistingTransaction);

        // =============================================================================================================
        // Getting a transaction and checking the results.
        GetTransactionResponse normalTransaction = mempoolTransactionService.getTransaction("46804b8a193cae200c99531f0ea90d81cc0c0e44e718b57e7b9ab5bb3926b946").block();
        assertNotNull(normalTransaction);
        assertEquals(754059, normalTransaction.getStatus().getBlockHeight().intValue());
        assertEquals(2, normalTransaction.getVout().size());
        Iterator<GetTransactionResponse.VOut> vOutsIterator = normalTransaction.getVout().iterator();

        // vOut 0.
        GetTransactionResponse.VOut vOut0 = vOutsIterator.next();
        assertEquals("76a914607c727d45ba97358a3fa06be04a514cfb12f4dd88ac", vOut0.getScriptPubKey());
        assertEquals("OP_DUP OP_HASH160 OP_PUSHBYTES_20 607c727d45ba97358a3fa06be04a514cfb12f4dd OP_EQUALVERIFY OP_CHECKSIG", vOut0.getScriptPubKeyAsm());
        assertEquals("p2pkh", vOut0.getScriptPubKeyType());
        assertEquals("19oAxAJhAKmEFb2pcA9qiX8sV8hxKeXjzd", vOut0.getScriptPubKeyAddress());
        assertEquals(0, new BigInteger("10000000").compareTo(vOut0.getValue()));

        // vOut 1.
        GetTransactionResponse.VOut vOut1 = vOutsIterator.next();
        assertEquals("0020701a8d401c84fb13e6baf169d59684e17abd9fa216c8cc5b9fc63d622ff8c58d", vOut1.getScriptPubKey());
        assertEquals("OP_0 OP_PUSHBYTES_32 701a8d401c84fb13e6baf169d59684e17abd9fa216c8cc5b9fc63d622ff8c58d", vOut1.getScriptPubKeyAsm());
        assertEquals("v0_p2wsh", vOut1.getScriptPubKeyType());
        assertEquals("bc1qwqdg6squsna38e46795at95yu9atm8azzmyvckulcc7kytlcckxswvvzej", vOut1.getScriptPubKeyAddress());
        assertEquals(0, new BigInteger("47765975").compareTo(vOut1.getValue()));

        // =============================================================================================================
        // Testing a taproot transaction.
        GetTransactionResponse taprootTransaction = mempoolTransactionService.getTransaction("d61a4957e5e756a7631246b1a00d685e4854f98f8c2835bafafed8b1d1e26be5").block();
        assertNotNull(taprootTransaction);
        assertEquals(742158, taprootTransaction.getStatus().getBlockHeight().intValue());
        assertEquals(2, taprootTransaction.getVout().size());

        // Taproot vOut.
        vOut0 = taprootTransaction.getVout().get(0);
        assertEquals("5120b037c6aa6784da1cfb00a5b0caaafd70556aa832cb6b67ba5e934f483d6a7f23", vOut0.getScriptPubKey());
        assertEquals("OP_PUSHNUM_1 OP_PUSHBYTES_32 b037c6aa6784da1cfb00a5b0caaafd70556aa832cb6b67ba5e934f483d6a7f23", vOut0.getScriptPubKeyAsm());
        assertEquals("v1_p2tr", vOut0.getScriptPubKeyType());
        assertEquals("bc1pkqmud2n8sndpe7cq5kcv42hawp2k42pjed4k0wj7jd85s0t20u3srt7l2r", vOut0.getScriptPubKeyAddress());
        assertEquals(0, new BigInteger("43124666573").compareTo(vOut0.getValue()));
    }

}
