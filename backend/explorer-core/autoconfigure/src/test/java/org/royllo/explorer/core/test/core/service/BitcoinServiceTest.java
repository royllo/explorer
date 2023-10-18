package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.test.MempoolData.BITCOIN_TAPROOT_TRANSACTION_2_TXID;
import static org.royllo.test.MempoolData.BITCOIN_TRANSACTION_1_TXID;
import static org.royllo.test.MempoolData.BITCOIN_TRANSACTION_2_TXID;
import static org.royllo.test.MempoolData.BITCOIN_TRANSACTION_3_TXID;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("BitcoinService tests")
public class BitcoinServiceTest extends TestWithMockServers {

    @Autowired
    private BitcoinService bitcoinService;

    @Test
    @DisplayName("getBitcoinTransactionOutput()")
    public void getBitcoinTransactionOutput() {
        Optional<BitcoinTransactionOutputDTO> bto;

        // =============================================================================================================
        // Getting a transaction output already in our database.
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_1_TXID, 0);
        assertTrue(bto.isPresent());
        verifyTransaction(bto.get(), BITCOIN_TRANSACTION_1_TXID);

        // =============================================================================================================
        // Getting a transaction output already in our database but with index 1 instead of index 0.
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_2_TXID, 1);
        assertTrue(bto.isPresent());
        verifyTransaction(bto.get(), BITCOIN_TRANSACTION_2_TXID);

        // =============================================================================================================
        // Getting a transaction output already in our database but the output doesn't exists.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_2_TXID, 0).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_2_TXID, 1).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_2_TXID, 2).isEmpty());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_2_TXID, 3).isEmpty());

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database or in the blockchain.
        assertFalse(bitcoinService.getBitcoinTransactionOutput("NON_EXISTING_TRANSACTION_OUTPUT", 0).isPresent());

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database but exists in the blockchain (index 0).
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 0);
        assertTrue(bto.isPresent());
        verifyTransaction(bto.get(), BITCOIN_TRANSACTION_3_TXID);

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database but exists in the blockchain (index 1).
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 1);
        assertTrue(bto.isPresent());
        verifyTransaction(bto.get(), BITCOIN_TRANSACTION_3_TXID);

        // =============================================================================================================
        // Getting again a transaction we saved in database. Check we did not create a duplicate.
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 1);
        assertTrue(bto.isPresent());
        assertEquals(7, bto.get().getId());

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database but exists in the blockchain.
        // But the output specified does not exist ! (There is two outputs, and we ask for index 4 - ie output 3).
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 3).isEmpty());

        // =============================================================================================================
        // Getting a taproot transaction directly from the blockchain.
        bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TAPROOT_TRANSACTION_2_TXID, 0);
        assertTrue(bto.isPresent());
        verifyTransaction(bto.get(), BITCOIN_TAPROOT_TRANSACTION_2_TXID);

        // =============================================================================================================
        // Checking others output indexes on our transaction in blockchain.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 0).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 1).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 2).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 3).isEmpty());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_3_TXID, 4).isEmpty());
    }

}
