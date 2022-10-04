package org.royllo.explorer.api.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.api.domain.universe.Update;
import org.royllo.explorer.api.dto.universe.UpdateDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/** Update related mapper. */
@Mapper(nullValuePropertyMappingStrategy = IGNORE, uses = UserMapper.class)
public interface UpdateMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    Update mapToUpdateRequest(UpdateDTO source);

    UpdateDTO mapToUpdateRequestDTO(Update source);

}
