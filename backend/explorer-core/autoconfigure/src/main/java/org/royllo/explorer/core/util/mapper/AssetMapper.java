package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.util.enums.AssetType;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Asset related mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE, uses = {BitcoinMapper.class, UserMapper.class})
public interface AssetMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    Asset mapToAsset(AssetDTO source);

    AssetDTO mapToAssetDTO(Asset source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(source = "asset.version", target = "version")
    @Mapping(source = "asset.assetGenesis.genesisPoint", target = "genesisPoint")
    @Mapping(source = "asset.assetGenesis.name", target = "name")
    @Mapping(source = "asset.assetGenesis.meta", target = "metaDataHash")
    @Mapping(source = "asset.assetGenesis.assetId", target = "assetId")
    @Mapping(source = "asset.assetGenesis.outputIndex", target = "outputIndex")
    @Mapping(source = "asset.assetGenesis.version", target = "genesisVersion")
    @Mapping(source = "asset.assetType", target = "type")
    @Mapping(source = "asset.amount", target = "amount")
    @Mapping(source = "asset.lockTime", target = "lockTime")
    @Mapping(source = "asset.relativeLockTime", target = "relativeLockTime")
    @Mapping(source = "asset.scriptVersion", target = "scriptVersion")
    @Mapping(source = "asset.scriptKey", target = "scriptKey")
    @Mapping(source = "asset.chainAnchor.anchorTx", target = "anchorTx")
    @Mapping(source = "asset.chainAnchor.anchorTxId", target = "anchorTxId")
    @Mapping(source = "asset.chainAnchor.anchorBlockHash", target = "anchorBlockHash")
    @Mapping(source = "asset.chainAnchor.anchorOutpoint", target = "anchorOutpoint")
    @Mapping(source = "asset.chainAnchor.internalKey", target = "internalKey")
    AssetDTO mapToAssetDTO(DecodedProofResponse.DecodedProof source);

    default BitcoinTransactionOutputDTO mapToBitcoinTransactionOutputDTO(final String source) {
        if (source != null) {
            return BitcoinTransactionOutputDTO.builder()
                    .txId(source.substring(0, source.indexOf(":")))
                    .vout(Integer.parseInt(source.substring(source.indexOf(":") + 1)))
                    .build();
        } else {
            return null;
        }
    }

    default AssetType mapToAssetType(final String source) {
        if (source.equals("COLLECTIBLE")) {
            return AssetType.COLLECTIBLE;
        }
        return AssetType.NORMAL;
    }

}
