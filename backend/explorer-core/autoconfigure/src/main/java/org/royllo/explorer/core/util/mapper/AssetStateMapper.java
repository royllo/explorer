package org.royllo.explorer.core.util.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.core.domain.asset.AssetState;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Asset state mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE,
        uses = {AssetGroupMapper.class, AssetMapper.class, BitcoinMapper.class, UserMapper.class})
@DecoratedWith(AssetStateMapperDecorator.class)
public interface AssetStateMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    AssetState mapToAssetState(AssetStateDTO source);

    AssetStateDTO mapToAssetStateDTO(AssetState source);

    // =================================================================================================================
    // Below are the mappings for the decoded proof response.

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "assetStateId", ignore = true)
    @Mapping(source = ".", target = "asset")
    @Mapping(source = "asset.chainAnchor.anchorBlockHash", target = "anchorBlockHash")
    @Mapping(source = "asset.chainAnchor.anchorOutpoint", target = "anchorOutpoint")
    @Mapping(source = "asset.chainAnchor.anchorTx", target = "anchorTx")
    @Mapping(source = "asset.chainAnchor.internalKey", target = "internalKey")
    @Mapping(source = "asset.chainAnchor.merkleRoot", target = "merkleRoot")
    @Mapping(source = "asset.chainAnchor.tapscriptSibling", target = "tapscriptSibling")
    @Mapping(source = "asset.scriptVersion", target = "scriptVersion")
    @Mapping(source = "asset.scriptKey", target = "scriptKey")
    AssetStateDTO mapToAssetStateDTO(DecodedProofResponse.DecodedProof source);

}
