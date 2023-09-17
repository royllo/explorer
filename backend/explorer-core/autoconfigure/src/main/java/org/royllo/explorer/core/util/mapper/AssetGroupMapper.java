package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.core.domain.asset.AssetGroup;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Asset group mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE,
        uses = {AssetMapper.class, BitcoinMapper.class, UserMapper.class})
public interface AssetGroupMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    AssetGroup mapToAssetGroup(AssetGroupDTO source);

    AssetGroupDTO mapToAssetGroupDTO(AssetGroup source);

    // =================================================================================================================
    // Below are the mappings for the decoded proof response.

}
