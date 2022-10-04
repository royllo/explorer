package org.royllo.explorer.api.service.user;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.domain.user.User;
import org.royllo.explorer.api.dto.user.UserDTO;
import org.royllo.explorer.api.repository.user.UserRepository;
import org.royllo.explorer.api.util.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.royllo.explorer.api.util.constants.UserConstants.ANONYMOUS_USER_UID;

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
        final Optional<User> anonymousUser = userRepository.findById(ANONYMOUS_USER_UID);
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
