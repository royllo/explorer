package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("mempoolTransactionServiceMock")
@DisplayName("BitcoinService tests")
public class BitcoinServiceTest extends BaseTest {

    @Autowired
    private BitcoinService bitcoinService;

    @Test
    @DisplayName("getBitcoinTransactionOutput()")
    public void getBitcoinTransactionOutputTest() {
        Optional<BitcoinTransactionOutputDTO> bto;

        // =============================================================================================================
        // Getting a transaction output already in our database.
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_1_TXID, 0);
        assertTrue(bto.isPresent());
        assertFalse(bto.get().isTaprootType());
        assertEquals(720, bto.get().getBlockHeight().intValue());
        assertEquals("2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea", bto.get().getTxId());
        assertEquals(0, bto.get().getVout());
        assertEquals("410415ca91c387efac4ea86f0196b2a1d831f75a488f115055636f0022c51df508197ad4fc31f553d48052b05b0a3b6030a84a0441adae97734129bb8ea0ddfd4004ac", bto.get().getScriptPubKey());
        assertEquals("OP_PUSHBYTES_65 0415ca91c387efac4ea86f0196b2a1d831f75a488f115055636f0022c51df508197ad4fc31f553d48052b05b0a3b6030a84a0441adae97734129bb8ea0ddfd4004 OP_CHECKSIG", bto.get().getScriptPubKeyAsm());
        assertEquals("p2pk", bto.get().getScriptPubKeyType());
        assertEquals("19oAxAJhAKmEFb2pcA9qiX8sV8hxKeXjzd", bto.get().getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("5000000000").compareTo(bto.get().getValue()));

        // =============================================================================================================
        // Getting a transaction output already in our database but with index 1 instead of index 0.
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_2_TXID, 1);
        assertTrue(bto.isPresent());
        assertEquals(766, bto.get().getBlockHeight().intValue());
        assertEquals("46804b8a193cae200c99531f0ea90d81cc0c0e44e718b57e7b9ab5bb3926b946", bto.get().getTxId());
        assertEquals(1, bto.get().getVout());
        assertEquals("0020701a8d401c84fb13e6baf169d59684e17abd9fa216c8cc5b9fc63d622ff8c58d", bto.get().getScriptPubKey());
        assertEquals("OP_0 OP_PUSHBYTES_32 701a8d401c84fb13e6baf169d59684e17abd9fa216c8cc5b9fc63d622ff8c58d", bto.get().getScriptPubKeyAsm());
        assertEquals("v0_p2wsh", bto.get().getScriptPubKeyType());
        assertEquals("bc1qwqdg6squsna38e46795at95yu9atm8azzmyvckulcc7kytlcckxswvvzej", bto.get().getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("47765975").compareTo(bto.get().getValue()));

        // =============================================================================================================
        // Getting a transaction output already in our database but the output doesn't exists.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_2_TXID, 0).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_2_TXID, 1).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_2_TXID, 2).isEmpty());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_2_TXID, 3).isEmpty());

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database or in the blockchain.
        assertFalse(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_NON_EXISTING, 0).isPresent());

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database but exists in the blockchain (index 1).
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 1);
        assertTrue(bto.isPresent());
        assertEquals(6, bto.get().getId());
        assertFalse(bto.get().isTaprootType());
        assertEquals(754381, bto.get().getBlockHeight());
        assertEquals("76a914611385f68799bfc360feacc3095de4e51a9c1bd688ac", bto.get().getScriptPubKey());
        assertEquals("OP_DUP OP_HASH160 OP_PUSHBYTES_20 611385f68799bfc360feacc3095de4e51a9c1bd6 OP_EQUALVERIFY OP_CHECKSIG", bto.get().getScriptPubKeyAsm());
        assertEquals("p2pkh", bto.get().getScriptPubKeyType());
        assertEquals("19rHw7N8TJgbJUrS6LCD3tz7KMyTDKAFwH", bto.get().getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("1048100").compareTo(bto.get().getValue()));

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database but exists in the blockchain (index 0).
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 0);
        assertTrue(bto.isPresent());
        assertEquals(7, bto.get().getId());
        assertFalse(bto.get().isTaprootType());
        assertEquals(754381, bto.get().getBlockHeight());
        assertEquals("76a9140aa7e954ae2c972225309f0992e3ecd698a90f5f88ac", bto.get().getScriptPubKey());
        assertEquals("OP_DUP OP_HASH160 OP_PUSHBYTES_20 0aa7e954ae2c972225309f0992e3ecd698a90f5f OP_EQUALVERIFY OP_CHECKSIG", bto.get().getScriptPubKeyAsm());
        assertEquals("p2pkh", bto.get().getScriptPubKeyType());
        assertEquals("1yLucPwZwVuNxMFTXyyX5qTk3YFNpAGEz", bto.get().getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("2036308").compareTo(bto.get().getValue()));

        // =============================================================================================================
        // Getting again a transaction we saved in database. Check we did not create a duplicate.
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 1);
        assertTrue(bto.isPresent());
        assertEquals(6, bto.get().getId());

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database but exists in the blockchain.
        // But the output specified does not exist ! (There is two outputs, and we ask for index 4 - ie output 3).
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 3).isEmpty());

        // =============================================================================================================
        // Getting a taproot transaction directly from the blockchain.
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TAPROOT_TRANSACTION_2_TXID, 0);
        assertTrue(bto.isPresent());
        assertEquals(8, bto.get().getId());
        assertTrue(bto.get().isTaprootType());
        assertEquals(742158, bto.get().getBlockHeight());
        assertEquals("5120b037c6aa6784da1cfb00a5b0caaafd70556aa832cb6b67ba5e934f483d6a7f23", bto.get().getScriptPubKey());
        assertEquals("OP_PUSHNUM_1 OP_PUSHBYTES_32 b037c6aa6784da1cfb00a5b0caaafd70556aa832cb6b67ba5e934f483d6a7f23", bto.get().getScriptPubKeyAsm());
        assertEquals("v1_p2tr", bto.get().getScriptPubKeyType());
        assertEquals("bc1pkqmud2n8sndpe7cq5kcv42hawp2k42pjed4k0wj7jd85s0t20u3srt7l2r", bto.get().getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("43124666573").compareTo(bto.get().getValue()));

        // =============================================================================================================
        // Checking others output indexes on our transaction in blockchain.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 0).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 1).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 2).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 3).isEmpty());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 4).isEmpty());

    }

}
