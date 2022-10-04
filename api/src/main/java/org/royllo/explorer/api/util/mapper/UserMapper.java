package org.royllo.explorer.api.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.royllo.explorer.api.dto.user.UserDTO;
import org.royllo.explorer.api.domain.user.User;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * User related mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE)
public interface UserMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    User mapToUser(UserDTO source);

    UserDTO mapToUserDTO(User source);

}
