package org.royllo.explorer.core.service.user;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.constants.UserConstants;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User service implementation.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImplementation extends BaseService implements UserService {

    /** User repository. */
    private final UserRepository userRepository;

    @Override
    public final UserDTO getAnonymousUser() {
        final Optional<User> anonymousUser = userRepository.findById(UserConstants.ANONYMOUS_USER_ID);
        if (anonymousUser.isPresent()) {
            return USER_MAPPER.mapToUserDTO(anonymousUser.get());
        } else {
            logger.error("Anonymous user not found - This should never happened");
            throw new RuntimeException("Anonymous user not found");
        }
    }

    @Override
    public final Optional<UserDTO> getUserByUsername(final String username) {
        return userRepository.findByUsername(username).map(USER_MAPPER::mapToUserDTO);
    }

}