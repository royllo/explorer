package org.royllo.explorer.core.dto.asset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
import static org.royllo.explorer.core.util.enums.FileType.UNKNOWN;

/**
 * Taproot asset.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AssetDTO {

    /** Minimum size of the asset ID alias. */
    public static final int ASSET_ID_ALIAS_MIN_SIZE = 3;

    /** Maximum size of the asset ID alias. */
    public static final int ASSET_ID_ALIAS_MAX_SIZE = 30;

    /** Maximum size of the readme. */
    public static final int README_MAX_SIZE = 3000;

    /** Unique identifier. */
    Long id;

    /** The asset creator. */
    @NotNull(message = "{validation.asset.creator.required}")
    UserDTO creator;

    /** Asset group. */
    AssetGroupDTO assetGroup;

    /** The asset ID that uniquely identifies the asset. */
    @NotBlank(message = "{validation.asset.assetId.required}")
    String assetId;

    /** The asset ID alias in Royllo (generated or configured). */
    @Size(min = ASSET_ID_ALIAS_MIN_SIZE, max = ASSET_ID_ALIAS_MAX_SIZE, message = "{validation.asset.assetIdAlias.size}")
    String assetIdAlias;

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    BitcoinTransactionOutputDTO genesisPoint;

    /** The name of the asset. */
    @NotBlank(message = "{validation.asset.assetName.required}")
    String name;

    /** The hash of the metadata for this genesis asset. */
    String metaDataHash;

    /** Meta data file name. */
    String metaDataFileName;

    /** The index of the output that carries the unique Taproot asset commitment in the genesis transaction. */
    @NotNull(message = "{validation.asset.outputIndex.required}")
    Integer outputIndex;

    /** The version of the Taproot asset. */
    @NotNull(message = "{validation.asset.version.required}")
    Integer version;

    /** The type of the asset: normal or a collectible. */
    @NotNull(message = "{validation.asset.assetType.required}")
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
            return UNKNOWN;
        }
        return FileType.getTypeByExtension(metaDataFileName.substring(metaDataFileName.lastIndexOf(".") + 1));
    }

    /** Readme content of the asset - Set by the owner. */
    @Size(max = README_MAX_SIZE, message = "{validation.asset.readme.size}")
    String readme;

}
