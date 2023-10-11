package org.royllo.explorer.core.test.integration.mempool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.mempool.GetTransactionResponse;
import org.royllo.explorer.core.provider.mempool.MempoolTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.test.MempoolData.BITCOIN_TAPROOT_TRANSACTION_2_TXID;
import static org.royllo.test.MempoolData.BITCOIN_TRANSACTION_2_TXID;

@SpringBootTest(properties = {"mempool.api.base-url=https://mempool.space/api"})
@DisplayName("Mempool transaction service test on mainnet")
public class MempoolTransactionServiceMainnetTest {

    @Autowired
    private MempoolTransactionService mempoolTransactionService;

    @Test
    @DisplayName("getTransaction() on mainnet")
    public void getTransactionTest() {
        // =============================================================================================================
        // Getting a non-existing transaction.
        final GetTransactionResponse nonExistingTransaction = mempoolTransactionService.getTransaction("NON-EXISTING-TRANSACTION").block();
        assertNull(nonExistingTransaction);

        // =============================================================================================================
        // Getting a transaction and checking the results.
        GetTransactionResponse normalTransaction = mempoolTransactionService.getTransaction(BITCOIN_TRANSACTION_2_TXID).block();
        assertNotNull(normalTransaction);
        assertEquals(754059, normalTransaction.getStatus().getBlockHeight().intValue());
        assertEquals(2, normalTransaction.getVout().size());
        Iterator<GetTransactionResponse.VOut> vOutsIterator = normalTransaction.getVout().iterator();

        // Vout 0.
        GetTransactionResponse.VOut vout0 = vOutsIterator.next();
        assertEquals("76a914607c727d45ba97358a3fa06be04a514cfb12f4dd88ac", vout0.getScriptPubKey());
        assertEquals("OP_DUP OP_HASH160 OP_PUSHBYTES_20 607c727d45ba97358a3fa06be04a514cfb12f4dd OP_EQUALVERIFY OP_CHECKSIG", vout0.getScriptPubKeyAsm());
        assertEquals("p2pkh", vout0.getScriptPubKeyType());
        assertEquals("19oAxAJhAKmEFb2pcA9qiX8sV8hxKeXjzd", vout0.getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("10000000").compareTo(vout0.getValue()));

        // Vout 1.
        GetTransactionResponse.VOut vout1 = vOutsIterator.next();
        assertEquals("0020701a8d401c84fb13e6baf169d59684e17abd9fa216c8cc5b9fc63d622ff8c58d", vout1.getScriptPubKey());
        assertEquals("OP_0 OP_PUSHBYTES_32 701a8d401c84fb13e6baf169d59684e17abd9fa216c8cc5b9fc63d622ff8c58d", vout1.getScriptPubKeyAsm());
        assertEquals("v0_p2wsh", vout1.getScriptPubKeyType());
        assertEquals("bc1qwqdg6squsna38e46795at95yu9atm8azzmyvckulcc7kytlcckxswvvzej", vout1.getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("47765975").compareTo(vout1.getValue()));

        // =============================================================================================================
        // Testing a taproot transaction.
        GetTransactionResponse taprootTransaction = mempoolTransactionService.getTransaction(BITCOIN_TAPROOT_TRANSACTION_2_TXID).block();
        assertNotNull(taprootTransaction);
        assertEquals(742158, taprootTransaction.getStatus().getBlockHeight().intValue());
        assertEquals(2, taprootTransaction.getVout().size());

        // Taproot vOut.
        vout0 = taprootTransaction.getVout().get(0);
        assertEquals("5120b037c6aa6784da1cfb00a5b0caaafd70556aa832cb6b67ba5e934f483d6a7f23", vout0.getScriptPubKey());
        assertEquals("OP_PUSHNUM_1 OP_PUSHBYTES_32 b037c6aa6784da1cfb00a5b0caaafd70556aa832cb6b67ba5e934f483d6a7f23", vout0.getScriptPubKeyAsm());
        assertEquals("v1_p2tr", vout0.getScriptPubKeyType());
        assertEquals("bc1pkqmud2n8sndpe7cq5kcv42hawp2k42pjed4k0wj7jd85s0t20u3srt7l2r", vout0.getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("43124666573").compareTo(vout0.getValue()));
    }

}
