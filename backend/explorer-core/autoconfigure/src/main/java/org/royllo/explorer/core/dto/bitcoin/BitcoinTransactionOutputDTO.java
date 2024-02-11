package org.royllo.explorer.core.dto.bitcoin;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

/**
 * Bitcoin transaction output.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class BitcoinTransactionOutputDTO {

    /** Unique identifier. */
    Long id;

    /** Block height. */
    Integer blockHeight;

    /** Block time. */
    @NotBlank(message = "{validation.bitcoin.blockTime.required}")
    LocalDateTime blockTime;

    /** Transaction id. */
    @NotBlank(message = "{validation.bitcoin.txId.required}")
    String txId;

    /** Output index (starts at 0). */
    int vout;

    /** ScriptPubKey is the script which controls how bitcoin can be spent. */
    String scriptPubKey;

    /** ScriptPubKey asm is the symbolic representation of the Bitcoin's Script language op-codes. */
    String scriptPubKeyAsm;

    /** ScriptPubKey type (p2pkh, p2wsh...). */
    String scriptPubKeyType;

    /** ScriptPubKey address. */
    String scriptPubKeyAddress;

    /** Value is the number of Satoshi (1 BTC = 100,000,000 Satoshi). */
    BigInteger value;

    /**
     * To string (only display txid-vout).
     *
     * @return txid + count
     */
    @Override
    public String toString() {
        return txId + ':' + vout;
    }

}
