package org.royllo.explorer.core.test.core.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("BitcoinTransactionOutputDTO tests")
public class BitcoinTransactionOutputDTOTest {

    @Test
    @DisplayName("getByteReverseTxId()")
    public void getByteReverseTxId() {
        var transaction = BitcoinTransactionOutputDTO.builder()
                .txId("6db79f5af2ba65bfb4044ced690f3acb4a791a6fc6a7450664e15559ad770b90")
                .build();
        assertEquals("900b77ad5955e1640645a7c66f1a794acb3a0f69ed4c04b4bf65baf25a9fb76d", transaction.getByteReverseTxId());
    }

}
