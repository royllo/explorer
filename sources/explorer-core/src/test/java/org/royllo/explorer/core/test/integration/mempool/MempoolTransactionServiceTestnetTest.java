package org.royllo.explorer.core.test.integration.mempool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.mempool.GetTransactionResponse;
import org.royllo.explorer.core.provider.mempool.MempoolTransactionService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"mempool.api.base-url=https://mempool.space/testnet/api"})
@DisplayName("Mempool transaction service testnet test")
public class MempoolTransactionServiceTestnetTest extends BaseTest {

    @Autowired
    private MempoolTransactionService mempoolTransactionService;

    @Test
    @DisplayName("getTransaction() on testnet")
    @SuppressWarnings("SpellCheckingInspection")
    public void getTransactionTest() {
        // =============================================================================================================
        // Testing a taproot transaction.
        GetTransactionResponse taprootTransaction = mempoolTransactionService.getTransaction(BITCOIN_TESTNET_TARO_TRANSACTION_1_TXID)
                .doOnError(throwable -> logger.error("Error getting transaction from mempool: {}", throwable.getMessage()))
                .block();
        assertNotNull(taprootTransaction);
        assertEquals(2348688, taprootTransaction.getStatus().getBlockHeight().intValue());
        assertEquals(2, taprootTransaction.getVout().size());

        // vOut 0.
        GetTransactionResponse.VOut vout0 = taprootTransaction.getVout().get(0);
        assertEquals("512092e007434a2583cd7b8f18a7e030ba281d35fefff4708d478cceb7a604c9ed20", vout0.getScriptPubKey());
        assertEquals("OP_PUSHNUM_1 OP_PUSHBYTES_32 92e007434a2583cd7b8f18a7e030ba281d35fefff4708d478cceb7a604c9ed20", vout0.getScriptPubKeyAsm());
        assertEquals("v1_p2tr", vout0.getScriptPubKeyType());
        assertEquals("tb1pjtsqws62ykpu67u0rzn7qv969qwntlhl73cg63uve6m6vpxfa5sq7lu9px", vout0.getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("1000").compareTo(vout0.getValue()));

        // vOut 1.
        GetTransactionResponse.VOut vout1 = taprootTransaction.getVout().get(1);
        assertEquals("00147fafce6a7ea1541dc33f99ab673dc525ed3a27b3", vout1.getScriptPubKey());
        assertEquals("OP_0 OP_PUSHBYTES_20 7fafce6a7ea1541dc33f99ab673dc525ed3a27b3", vout1.getScriptPubKeyAsm());
        assertEquals("v0_p2wpkh", vout1.getScriptPubKeyType());
        assertEquals("tb1q07huu6n7592pmselnx4kw0w9yhkn5fan74dze9", vout1.getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("1699422").compareTo(vout1.getValue()));

    }

}
