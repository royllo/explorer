package org.royllo.explorer.core.dto.asset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.util.enums.AssetType;

import java.math.BigInteger;

import static lombok.AccessLevel.PRIVATE;

/**
 * Taproot asset.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AssetDTO {

    /** Unique identifier. */
    Long id;

    /** The asset creator. */
    @NotNull(message = "Asset creator is required")
    UserDTO creator;

    /** The version of the Taproot asset. */
    @NotNull(message = "The version of the Taproot asset is required")
    Integer version;

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    BitcoinTransactionOutputDTO genesisPoint;

    /** The name of the asset. */
    @NotBlank(message = "Asset name is required")
    String name;

    /** The hash of the metadata for this genesis asset. */
    @NotBlank(message = "Hashed metadata is required")
    String metaDataHash;

    /** The asset ID that uniquely identifies the asset. */
    @NotBlank(message = "Asset id is required")
    String assetId;

    /** The index of the output that carries the unique Taproot asset commitment in the genesis transaction. */
    @NotNull(message = "Output index is required")
    Integer outputIndex;

    /** The version of the Taproot asset commitment that created this asset. */
    @NotNull(message = "Genesis version is required")
    Integer genesisVersion;

    /** The type of the asset: normal or a collectible. */
    @NotNull(message = "Asset type is required")
    AssetType type;

    /** The total amount of the asset stored in this Taproot asset UTXO. */
    @NotNull(message = "Total amount is required")
    BigInteger amount;

    /** An optional lock time, as with Bitcoin transactions. */
    @NotNull(message = "Lock time is required")
    Integer lockTime;

    /** An optional relative lock time, as with Bitcoin transactions. */
    @NotNull(message = "Relative lock time is required")
    Integer relativeLockTime;

    /** The version of the script, only version 0 is defined at present. */
    @NotNull(message = "Script version is required")
    Integer scriptVersion;

    /** The script key of the asset, which can be spent under Taproot semantics. */
    @NotNull(message = "Script key is required")
    String scriptKey;

    /** Asset group: The raw group key which is a normal public key. */
    String rawGroupKey;

    /** Asset group: The tweaked group key, which is derived based on the genesis point and also asset type. */
    String tweakedGroupKey;

    /** Asset group: A signature over the genesis point using the above key. */
    String assetIdSig;

}
