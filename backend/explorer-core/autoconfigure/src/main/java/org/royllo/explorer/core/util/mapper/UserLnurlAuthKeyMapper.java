package org.royllo.explorer.core.util.mapper;

import org.mapstruct.Mapper;
import org.royllo.explorer.core.domain.user.UserLnurlAuthKey;
import org.royllo.explorer.core.dto.user.UserLnurlAuthKeyDTO;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * User LNURL auth key related mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = IGNORE,
        uses = {UserMapper.class})
public interface UserLnurlAuthKeyMapper {

    UserLnurlAuthKey mapToUserLnurlAuthKey(UserLnurlAuthKeyDTO source);

    UserLnurlAuthKeyDTO mapToUserLnurlAuthKeyDTO(UserLnurlAuthKey source);

}
