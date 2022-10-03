package org.royllo.explorer.backend.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.backend.domain.universe.Update;
import org.royllo.explorer.backend.dto.universe.UpdateDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/** Update related mapper. */
@Mapper(nullValuePropertyMappingStrategy = IGNORE, uses = UserMapper.class)
public interface UpdateMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    Update mapToUpdateRequest(UpdateDTO source);

    UpdateDTO mapToUpdateRequestDTO(Update source);

}
