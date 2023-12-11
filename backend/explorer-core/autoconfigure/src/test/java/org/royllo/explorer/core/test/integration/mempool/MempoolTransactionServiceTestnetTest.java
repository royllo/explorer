package org.royllo.explorer.core.test.integration.mempool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.mempool.GetTransactionResponse;
import org.royllo.explorer.core.provider.mempool.MempoolTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(properties = {"mempool.api.base-url=https://mempool.space/testnet/api"})
@DisplayName("Mempool transaction service on testnet")
public class MempoolTransactionServiceTestnetTest {

    @Autowired
    private MempoolTransactionService mempoolTransactionService;

    @Test
    @DisplayName("getTransaction() on testnet")
    @SuppressWarnings("SpellCheckingInspection")
    public void getTransactionTest() {
        // =============================================================================================================
        // Getting a non-existing transaction.
        final GetTransactionResponse nonExistingTransaction = mempoolTransactionService.getTransaction("NON_EXISTING_TRANSACTION_ID").block();
        assertNull(nonExistingTransaction);

        // =============================================================================================================
        // Testing a taproot transaction.
        // curl https://mempool.space/testnet/api/tx/d8a8016095b9fcd1f63c57342d375026ecbc72c885a54b676c6e62b216e15365 | jq
        GetTransactionResponse taprootTransaction = mempoolTransactionService.getTransaction("d8a8016095b9fcd1f63c57342d375026ecbc72c885a54b676c6e62b216e15365").block();
        assertNotNull(taprootTransaction);
        assertEquals(2348688, taprootTransaction.getStatus().getBlockHeight().intValue());
        assertEquals(1664417898, taprootTransaction.getStatus().getBlockTime().longValue());
        assertEquals(2, taprootTransaction.getVout().size());

        // vOut 0.
        GetTransactionResponse.VOut vOut0 = taprootTransaction.getVout().get(0);
        assertEquals("512092e007434a2583cd7b8f18a7e030ba281d35fefff4708d478cceb7a604c9ed20", vOut0.getScriptPubKey());
        assertEquals("OP_PUSHNUM_1 OP_PUSHBYTES_32 92e007434a2583cd7b8f18a7e030ba281d35fefff4708d478cceb7a604c9ed20", vOut0.getScriptPubKeyAsm());
        assertEquals("v1_p2tr", vOut0.getScriptPubKeyType());
        assertEquals("tb1pjtsqws62ykpu67u0rzn7qv969qwntlhl73cg63uve6m6vpxfa5sq7lu9px", vOut0.getScriptPubKeyAddress());
        assertEquals(0, new BigInteger("1000").compareTo(vOut0.getValue()));

        // vOut 1.
        GetTransactionResponse.VOut vOut1 = taprootTransaction.getVout().get(1);
        assertEquals("00147fafce6a7ea1541dc33f99ab673dc525ed3a27b3", vOut1.getScriptPubKey());
        assertEquals("OP_0 OP_PUSHBYTES_20 7fafce6a7ea1541dc33f99ab673dc525ed3a27b3", vOut1.getScriptPubKeyAsm());
        assertEquals("v0_p2wpkh", vOut1.getScriptPubKeyType());
        assertEquals("tb1q07huu6n7592pmselnx4kw0w9yhkn5fan74dze9", vOut1.getScriptPubKeyAddress());
        assertEquals(0, new BigInteger("1699422").compareTo(vOut1.getValue()));
    }

}
