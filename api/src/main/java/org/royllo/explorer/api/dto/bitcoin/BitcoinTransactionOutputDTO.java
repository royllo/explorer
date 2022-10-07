package org.royllo.explorer.api.dto.bitcoin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

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

    /** Transaction id. */
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
    BigDecimal value;

    /**
     * Is this transaction output a taproot transaction ?
     *
     * @return true if it's a taproot transaction
     */
    public boolean isTaprootType() {
        return getScriptPubKeyType() != null && getScriptPubKeyType().toLowerCase().endsWith("p2tr");
    }

}
