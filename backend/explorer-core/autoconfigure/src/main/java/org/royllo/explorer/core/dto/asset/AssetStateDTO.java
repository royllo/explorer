package org.royllo.explorer.core.dto.asset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.dto.user.UserDTO;

import static lombok.AccessLevel.PRIVATE;

/**
 * Taproot asset state.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AssetStateDTO {

    /** Unique identifier. */
    Long id;

    /** The asset creator. */
    @NotNull(message = "Asset state creator is required")
    UserDTO creator;

    /** The asset. */
    @NotNull(message = "Linked asset is required")
    AssetDTO asset;

    /** The transaction that anchors the Taproot asset commitment where the asset resides. */
    @NotNull(message = "Anchor transaction is required")
    String anchorTx;

    /** The block hash the contains the anchor transaction above. */
    @NotNull(message = "Anchor block hash is required")
    String anchorBlockHash;

    /** Outpoint (txid:vout) that stores the Taproot asset commitment. */
    @NotNull(message = "Anchor outpoint is required")
    BitcoinTransactionOutputDTO anchorOutpoint;

    /** The raw internal key that was used to create the anchor Taproot output key. */
    @NotNull(message = "Anchor internal key is required")
    String internalKey;

    /** The Taproot merkle root hash of the anchor output the asset was committed to. */
    String merkleRoot;

    /** The serialized preimage of a Tapscript sibling, if there was one. */
    String tapscriptSibling;

    /** The version of the script, only version 0 is defined at present. */
    @NotNull(message = "Script version is required")
    Integer scriptVersion;

    /** The script key of the asset, which can be spent under Taproot semantics. */
    @NotNull(message = "Script key is required")
    String scriptKey;

    /** The asset state ID that uniquely identifies the asset state (calculated by Royllo). */
    @Setter
    @NonFinal
    @NotBlank(message = "Asset state id is required")
    String assetStateId;

}
