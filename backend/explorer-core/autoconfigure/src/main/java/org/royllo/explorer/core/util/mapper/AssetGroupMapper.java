package org.royllo.explorer.core.util.mapper;

import org.mapstruct.DecoratedWith;
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
@DecoratedWith(AssetGroupMapperDecorator.class)
public interface AssetGroupMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    AssetGroup mapToAssetGroup(AssetGroupDTO source);

    @Mapping(target = "assets", source = "assets")
    AssetGroupDTO mapToAssetGroupDTO(AssetGroup source);

}
