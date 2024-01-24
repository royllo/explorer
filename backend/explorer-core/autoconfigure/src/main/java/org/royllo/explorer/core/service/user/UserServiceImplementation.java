package org.royllo.explorer.core.service.user;

import io.micrometer.common.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.royllo.explorer.core.util.constants.AdministratorUserConstants.ADMINISTRATOR_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.enums.UserRole.USER;

/**
 * {@link UserService} implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class UserServiceImplementation extends BaseService implements UserService {

    /** User repository. */
    private final UserRepository userRepository;

    @Override
    public UserDTO getAdministratorUser() {
        logger.info("Getting administrator user");

        final Optional<User> administratorUser = userRepository.findById(ADMINISTRATOR_ID);
        if (administratorUser.isPresent()) {
            logger.info("Returning administrator user");
            return USER_MAPPER.mapToUserDTO(administratorUser.get());
        } else {
            logger.error("Administrator user not found - This should never happened");
            throw new RuntimeException("Administrator user not found");
        }
    }

    @Override
    public UserDTO getAnonymousUser() {
        logger.info("Getting anonymous user");

        final Optional<User> anonymousUser = userRepository.findById(ANONYMOUS_ID);
        if (anonymousUser.isPresent()) {
            logger.info("Returning anonymous user");
            return USER_MAPPER.mapToUserDTO(anonymousUser.get());
        } else {
            logger.error("Anonymous user not found - This should never happened");
            throw new RuntimeException("Anonymous user not found");
        }
    }

    @Override
    public UserDTO createUser(final String username) {
        logger.info("Creating a user with username: {}", username);

        // Verification.
        assert StringUtils.isNotEmpty(username) : "Username is required";
        assert userRepository.findByUsernameIgnoreCase(username.trim()).isEmpty() : "Username '" + username + "' already registered";

        // Creation.
        final User userCreated = userRepository.save(User.builder()
                .userId(UUID.randomUUID().toString())
                .username(username.toLowerCase().trim())
                .role(USER)
                .build());

        logger.info("User created: {}", userCreated);
        return USER_MAPPER.mapToUserDTO(userCreated);
    }

    @Override
    public Optional<UserDTO> getUserByUserId(@NonNull final String userId) {
        logger.info("Getting user with userId: {}", userId);

        final Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            logger.info("User with user id {} not found", userId);
            return Optional.empty();
        } else {
            logger.info("User with user id '{}' found: {}", userId, user.get());
            return user.map(USER_MAPPER::mapToUserDTO);
        }
    }

    @Override
    public Optional<UserDTO> getUserByUsername(@NonNull final String username) {
        logger.info("Getting user with username: {}", username);

        final Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
        if (user.isEmpty()) {
            logger.info("User with username {} not found", username);
            return Optional.empty();
        } else {
            logger.info("User with username '{}' found: {}", username, user.get());
            return user.map(USER_MAPPER::mapToUserDTO);
        }
    }

}
