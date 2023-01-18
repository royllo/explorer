package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.dto.asset.AssetDTO;

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

}
