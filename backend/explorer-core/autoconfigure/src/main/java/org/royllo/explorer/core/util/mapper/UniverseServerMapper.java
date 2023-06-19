package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.core.domain.universe.UniverseServer;
import org.royllo.explorer.core.dto.universe.UniverseServerDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Universe server mapper.
 */
@SuppressWarnings("unused")
@Mapper(nullValuePropertyMappingStrategy = IGNORE, uses = {UserMapper.class})
public interface UniverseServerMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    UniverseServer mapToUniverseServer(UniverseServerDTO source);

    UniverseServerDTO mapToUniverseServerDTO(UniverseServer source);

}
