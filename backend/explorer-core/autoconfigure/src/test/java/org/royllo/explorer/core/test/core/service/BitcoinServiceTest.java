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
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;
import static org.royllo.test.MempoolData.UNKNOWN_ROYLLO_COIN_GENESIS_TXID;

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
        bto = bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 0);
        assertTrue(bto.isPresent());
        verifyTransaction(bto.get(), ROYLLO_COIN_GENESIS_TXID);

        // =============================================================================================================
        // Getting a transaction output already in our database but with index 1 instead of index 0.
        bto = bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 1);
        assertTrue(bto.isPresent());
        verifyTransaction(bto.get(), ROYLLO_COIN_GENESIS_TXID);

        // =============================================================================================================
        // Getting a transaction output already in our database but the output doesn't exists.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 0).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 1).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 24).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 25).isEmpty());

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database or in the blockchain.
        assertFalse(bitcoinService.getBitcoinTransactionOutput("NON_EXISTING_TRANSACTION_OUTPUT", 0).isPresent());

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database but exists in the blockchain (index 0).
        bto = bitcoinService.getBitcoinTransactionOutput(UNKNOWN_ROYLLO_COIN_GENESIS_TXID, 0);
        assertTrue(bto.isPresent());
        verifyTransaction(bto.get(), UNKNOWN_ROYLLO_COIN_GENESIS_TXID);
        final Long outputId = bto.get().getId();

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database but exists in the blockchain (index 1).
        bto = bitcoinService.getBitcoinTransactionOutput(UNKNOWN_ROYLLO_COIN_GENESIS_TXID, 1);
        assertTrue(bto.isPresent());
        verifyTransaction(bto.get(), UNKNOWN_ROYLLO_COIN_GENESIS_TXID);

        // =============================================================================================================
        // Getting again a transaction we saved in database. Check we did not create a duplicate.
        bto = bitcoinService.getBitcoinTransactionOutput(UNKNOWN_ROYLLO_COIN_GENESIS_TXID, 0);
        assertTrue(bto.isPresent());
        assertEquals(outputId, bto.get().getId());

        // =============================================================================================================
        // Getting a transaction that doesn't exist in our database but exists in the blockchain.
        // But the output specified does not exist !
        assertTrue(bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 25).isEmpty());
    }

}
