package org.royllo.explorer.core.util.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.util.enums.AssetType;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;
import static org.royllo.explorer.core.util.enums.AssetType.COLLECTIBLE;
import static org.royllo.explorer.core.util.enums.AssetType.NORMAL;

/**
 * Asset mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE,
        uses = {AssetGroupMapper.class, BitcoinMapper.class, UserMapper.class})
@DecoratedWith(AssetMapperDecorator.class)
public interface AssetMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    Asset mapToAsset(AssetDTO source);

    AssetDTO mapToAssetDTO(Asset source);

    // =================================================================================================================
    // Below are the mappings for the decoded proof response.

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(source = "tweakedGroupKey", target = "assetGroupId")
    @Mapping(source = "assetWitness", target = "assetWitness")
    @Mapping(source = "rawGroupKey", target = "rawGroupKey")
    @Mapping(source = "tweakedGroupKey", target = "tweakedGroupKey")
    AssetGroupDTO mapToAssetGroupDTO(DecodedProofResponse.DecodedProof.Asset.AssetGroup source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assetIdAlias", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(source = "asset.assetGroup", target = "assetGroup")
    @Mapping(source = "asset.assetGenesis.assetId", target = "assetId")
    @Mapping(source = "asset.assetGenesis.genesisPoint", target = "genesisPoint")
    @Mapping(source = "asset.assetGenesis.metaDataHash", target = "metaDataHash")
    @Mapping(source = "asset.assetGenesis.name", target = "name")
    @Mapping(source = "asset.assetGenesis.outputIndex", target = "outputIndex")
    @Mapping(source = "asset.assetGenesis.version", target = "version")
    @Mapping(source = "asset.assetGenesis.assetType", target = "type")
    @Mapping(source = "asset.amount", target = "amount")
    @Mapping(target = "metaDataFileName", ignore = true)
    @Mapping(target = "issuanceDate", ignore = true)
    @Mapping(target = "readme", ignore = true)
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
        if (source.equalsIgnoreCase("COLLECTIBLE")) {
            return COLLECTIBLE;
        }
        return NORMAL;
    }

}
