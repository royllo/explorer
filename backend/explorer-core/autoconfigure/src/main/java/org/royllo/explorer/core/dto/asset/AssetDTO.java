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

    /** The asset ID that uniquely identifies the asset. */
    @NotBlank(message = "Asset id is required")
    String assetId;

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    BitcoinTransactionOutputDTO genesisPoint;

    /** The hash of the metadata for this genesis asset. */
    String metaDataHash;

    /** The name of the asset. */
    @NotBlank(message = "Asset name is required")
    String name;

    /** The index of the output that carries the unique Taproot asset commitment in the genesis transaction. */
    @NotNull(message = "Output index is required")
    Integer outputIndex;

    /** The version of the Taproot asset. */
    @NotNull(message = "The version of the Taproot asset is required")
    Integer version;

    /** The type of the asset: normal or a collectible. */
    @NotNull(message = "Asset type is required")
    AssetType type;

    /** The total amount of the asset stored in this Taproot asset UTXO. */
    @NotNull(message = "Total amount is required")
    BigInteger amount;

    /** Asset group. */
    @NonFinal
    @Setter
    AssetGroupDTO assetGroup;

}
