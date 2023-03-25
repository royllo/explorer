package org.royllo.explorer.core.service.user;

import lombok.NonNull;
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
        logger.info("getAnonymousUser - Getting anonymous user");

        final Optional<User> anonymousUser = userRepository.findById(UserConstants.ANONYMOUS_ID);
        if (anonymousUser.isPresent()) {
            logger.error("getAnonymousUser - Returning anonymous user");
            return USER_MAPPER.mapToUserDTO(anonymousUser.get());
        } else {
            logger.error("getAnonymousUser - Anonymous user not found - This should never happened");
            throw new RuntimeException("Anonymous user not found");
        }
    }

    @Override
    public final Optional<UserDTO> getUserByUserId(@NonNull final String userId) {
        logger.info("getUserByUserId - Getting user with userId: {}" + userId);

        final Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            logger.info("getUserByUserId - User with user id {} not found", userId);
            return Optional.empty();
        } else {
            logger.info("getUserByUserId - User with user id '{}' found: {}", userId, user.get());
            return user.map(USER_MAPPER::mapToUserDTO);
        }
    }

    @Override
    public final Optional<UserDTO> getUserByUsername(@NonNull final String username) {
        logger.info("getUserByUserId - Getting user with username: {}" + username);

        final Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            logger.info("getUserByUsername - User with username {} not found", username);
            return Optional.empty();
        } else {
            logger.info("getUserByUsername - User with username '{}' found: {}", username, user.get());
            return user.map(USER_MAPPER::mapToUserDTO);
        }
    }

}
