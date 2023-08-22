package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.util.enums.AssetType;

import static org.mapstruct.CollectionMappingStrategy.ADDER_PREFERRED;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Asset mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE,
        collectionMappingStrategy = ADDER_PREFERRED,
        uses = {BitcoinMapper.class, UserMapper.class})
public interface AssetMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "assetGroup", ignore = true)
    Asset mapToAsset(AssetDTO source);

    @Mapping(target = "assetGroup", ignore = true)
    AssetDTO mapToAssetDTO(Asset source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(source = "asset.assetGroup.assetIdSig", target = "assetIdSig")
    @Mapping(source = "asset.assetGroup.rawGroupKey", target = "rawGroupKey")
    @Mapping(source = "asset.assetGroup.tweakedGroupKey", target = "tweakedGroupKey")
    AssetGroupDTO mapToAssetGroupDTO(DecodedProofResponse.DecodedProof source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "assetGroup", ignore = true)
    @Mapping(source = "asset.assetGenesis.assetId", target = "assetId")
    @Mapping(source = "asset.assetGenesis.genesisPoint", target = "genesisPoint")
    @Mapping(source = "asset.assetGenesis.metaDataHash", target = "metaDataHash")
    @Mapping(source = "asset.assetGenesis.name", target = "name")
    @Mapping(source = "asset.assetGenesis.outputIndex", target = "outputIndex")
    @Mapping(source = "asset.assetGenesis.version", target = "version")
    @Mapping(source = "asset.assetType", target = "type")
    @Mapping(source = "asset.amount", target = "amount")
    AssetDTO mapToAssetDTO(DecodedProofResponse.DecodedProof source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(source = "asset.chainAnchor.anchorBlockHash", target = "anchorBlockHash")
    @Mapping(source = "asset.chainAnchor.anchorOutpoint", target = "anchorOutpoint")
    @Mapping(source = "asset.chainAnchor.anchorTx", target = "anchorTx")
    @Mapping(source = "asset.chainAnchor.anchorTxId", target = "anchorTxId")
    @Mapping(source = "asset.chainAnchor.internalKey", target = "internalKey")
    @Mapping(source = "asset.chainAnchor.merkleRoot", target = "merkleRoot")
    @Mapping(source = "asset.chainAnchor.tapscriptSibling", target = "tapscriptSibling")
    @Mapping(source = "asset.scriptVersion", target = "scriptVersion")
    @Mapping(source = "asset.scriptKey", target = "scriptKey")
    @Mapping(source = "asset.version", target = "version")
    AssetStateDTO mapToAssetStateDTO(DecodedProofResponse.DecodedProof source);

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
