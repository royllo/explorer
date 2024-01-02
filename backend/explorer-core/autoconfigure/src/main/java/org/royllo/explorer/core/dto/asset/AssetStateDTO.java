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

import java.math.BigInteger;
import java.util.List;

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

    /** The version of the Taproot Asset. */
    @NotNull(message = "Version is required")
    String version;

    /** The total amount of the asset stored in this Taproot asset UTXO. */
    @NotNull(message = "Amount is required")
    BigInteger amount;

    /** An optional locktime, as with Bitcoin transactions. */
    int lockTime;

    /** An optional relative lock time, same as Bitcoin transactions. */
    int relativeLockTime;

    /** The version of the script, only version 0 is defined at present. */
    @NotNull(message = "Script version is required")
    Integer scriptVersion;

    /** The script key of the asset, which can be spent under Taproot semantics. */
    @NotNull(message = "Script key is required")
    String scriptKey;

    /** Indicates whether the asset has been spent. */
    boolean spent;

    /** If the asset has been leased, this is the owner (application ID) of the lease. */
    String leaseOwner;

    /** If the asset has been leased, this is the expiry of the lease as a Unix timestamp in seconds. */
    Long leaseExpiry;

    /** Indicates whether this transfer was an asset burn. If true, the number of assets in this output are destroyed and can no longer be spent. */
    boolean burn;

    /** The merkle proof for AnchorTx used to prove its inclusion within BlockHeader. */
    String txMerkleProof;

    /** The TaprootProof proving the new inclusion of the resulting asset within AnchorTx. */
    String inclusionProof;

    /** The set of TaprootProofs proving the exclusion of the resulting asset from all other Taproot outputs within AnchorTx. */
    List<String> exclusionProofs;

    /** An optional TaprootProof needed if this asset is the result of a split. SplitRootProof proves inclusion of the root asset of the split. */
    String splitRootProof;

    /** ChallengeWitness is an optional virtual transaction witness that serves as an ownership proof for the asset. */
    List<String> challengeWitness;

    /** The asset state ID that uniquely identifies the asset state (calculated by Royllo). */
    @Setter
    @NonFinal
    @NotBlank(message = "Asset state id is required")
    String assetStateId;

    /** Indicates if this asset state is an issuance or a transfer. */
    boolean issuance;

}
