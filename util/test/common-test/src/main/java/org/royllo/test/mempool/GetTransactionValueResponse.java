package org.royllo.test.mempool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Get transaction response.
 * <a href="https://mempool.space/ja/docs/api/rest#get-transaction">Documentation</a>
 * <a href="https://mempool.space/api/tx/15e10745f15593a899cef391191bdd3d7c12412cc4696b7bcb669d0feadc8521">Example</a>
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class GetTransactionValueResponse {

    /** Bitcoin transaction id. */
    @JsonProperty("txid")
    String txId;

    /** status. **/
    Status status = null;

    /** Outputs. */
    List<VOut> vout = new LinkedList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {

        /** Block height. */
        @JsonProperty("block_height")
        Integer blockHeight = null;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @SuppressWarnings("checkstyle:VisibilityModifier")
    public static class VOut {

        /** ScriptPubKey is the script which controls how bitcoin can be spent. */
        @JsonProperty("scriptpubkey")
        String scriptPubKey = null;

        /** ScriptPubKey asm is the symbolic representation of the Bitcoin's Script language op-codes. */
        @JsonProperty("scriptpubkey_asm")
        String scriptPubKeyAsm = null;

        /** ScriptPubKey type (p2pkh, p2wsh...). */
        @JsonProperty("scriptpubkey_type")
        String scriptPubKeyType = null;

        /** ScriptPubKey address. */
        @JsonProperty("scriptpubkey_address")
        String scriptPubKeyAddress = null;

        /** Value is the number of Satoshi (1 BTC = 100,000,000 Satoshi). */
        @JsonProperty("value")
        BigDecimal value = null;

    }

}
