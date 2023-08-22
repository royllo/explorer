package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.core.domain.asset.AssetState;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Asset state mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE, uses = {AssetMapper.class, BitcoinMapper.class, UserMapper.class})
public interface AssetStateMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "asset", ignore = true)
    AssetState mapToAssetState(AssetStateDTO source);

    AssetStateDTO mapToAsseGroupDTO(AssetState source);

}
