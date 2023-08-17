package org.royllo.explorer.core.dto.asset;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

/**
 * Taproot asset chain anchor.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ChainAnchorDTO {

    // TODO Add a link to a bitcoin transaction output DTO

    /** Unique identifier. */
    Long id;

    /** The block hash the contains the anchor transaction above. */
    @NotNull(message = "Anchor block hash is required")
    String anchorBlockHash;

    /** Outpoint (txid:vout) that stores the Taproot asset commitment. */
    @NotNull(message = "Anchor outpoint is required")
    String anchorOutpoint;

    /** The transaction that anchors the Taproot asset commitment where the asset resides. */
    @NotNull(message = "Anchor transaction is required")
    String anchorTx;

    /** The txid of the anchor transaction. */
    @NotNull(message = "Anchor transaction id is required")
    String anchorTxId;

    /** The raw internal key that was used to create the anchor Taproot output key. */
    @NotNull(message = "Anchor internal key is required")
    String internalKey;

    /** The Taproot merkle root hash of the anchor output the asset was committed to. */
    String merkleRoot;

    /** The serialized preimage of a Tapscript sibling, if there was one. */
    String tapscriptSibling;

}
