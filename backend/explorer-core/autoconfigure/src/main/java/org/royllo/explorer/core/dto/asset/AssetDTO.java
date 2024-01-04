package org.royllo.explorer.core.dto.asset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.util.enums.AssetType;
import org.royllo.explorer.core.util.enums.FileType;

import java.math.BigInteger;
import java.time.ZonedDateTime;

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

    /** Asset group. */
    AssetGroupDTO assetGroup;

    /** The asset ID that uniquely identifies the asset. */
    @NotBlank(message = "Asset id is required")
    String assetId;

    /** The asset ID alias in Royllo (generated or configured). */
    String assetIdAlias;

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    BitcoinTransactionOutputDTO genesisPoint;

    /** The name of the asset. */
    @NotBlank(message = "Asset name is required")
    String name;

    /** The hash of the metadata for this genesis asset. */
    String metaDataHash;

    /** Meta data file name. */
    String metaDataFileName;

    /** The index of the output that carries the unique Taproot asset commitment in the genesis transaction. */
    @NotNull(message = "Output index is required")
    Integer outputIndex;

    /** The version of the Taproot asset. */
    @NotNull(message = "The version of the Taproot asset is required")
    Integer version;

    /** The type of the asset: normal or a collectible. */
    @NotNull(message = "Asset type is required")
    AssetType type;

    /** The total amount minted for this asset. */
    BigInteger amount;

    /** The date and time when the asset was created. */
    ZonedDateTime issuanceDate;

    /**
     * Returns the type of the metadata file.
     *
     * @return file type
     */
    public FileType getMetaDataFileType() {
        if (StringUtils.isBlank(metaDataFileName) || metaDataFileName.lastIndexOf(".") == -1) {
            return FileType.UNKNOWN;
        }
        return FileType.getTypeByExtension(metaDataFileName.substring(metaDataFileName.lastIndexOf(".") + 1));
    }

}
