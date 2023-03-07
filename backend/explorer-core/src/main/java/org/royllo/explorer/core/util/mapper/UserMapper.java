package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.dto.user.UserDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * User related mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE)
public interface UserMapper {

    User mapToUser(UserDTO source);

    UserDTO mapToUserDTO(User source);

}
