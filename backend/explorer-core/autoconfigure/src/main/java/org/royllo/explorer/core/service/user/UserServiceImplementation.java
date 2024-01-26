package org.royllo.explorer.core.service.user;

import io.micrometer.common.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.domain.user.UserLnurlAuthKey;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.repository.user.UserLnurlAuthKeyRepository;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.stereotype.Service;
import org.tbk.lnurl.auth.K1;
import org.tbk.lnurl.auth.LinkingKey;
import org.tbk.lnurl.auth.LnurlAuthPairingService;
import org.tbk.lnurl.simple.auth.SimpleLinkingKey;

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
public class UserServiceImplementation extends BaseService implements UserService, LnurlAuthPairingService {

    /** User repository. */
    private final UserRepository userRepository;

    /** User lnurl-auth key repository. */
    private final UserLnurlAuthKeyRepository userLnurlAuthKeyRepository;

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
                .username(username.trim().toLowerCase())
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

    @Override
    public boolean pairK1WithLinkingKey(@NonNull final K1 k1, @NonNull final LinkingKey linkingKey) {
        // This method is called by the spring boot starter when a user has provided a k1 signed with a linking key and everything is valid.
        // Usually, this is where you search if the linking key already exists in the database.
        // If not, you create a new user and store the linking key.
        // If yes, you just return the user, and you update the k1 used.
        final Optional<UserLnurlAuthKey> linkingKeyInDatabase = userLnurlAuthKeyRepository.findByLinkingKey(linkingKey.toHex());
        if (linkingKeyInDatabase.isEmpty()) {
            logger.info("Creating the user for the linking key: {}", linkingKey.toHex());
            // We create the user.
            final UserDTO user = createUser(linkingKey.toHex());
            // We create the user lnurl-auth key.
            userLnurlAuthKeyRepository.save(UserLnurlAuthKey.builder()
                    .owner(USER_MAPPER.mapToUser(user))
                    .linkingKey(linkingKey.toHex())
                    .k1(k1.toHex())
                    .build());
        } else {
            logger.info("User with the linking key {} exists", linkingKey.toHex());
            linkingKeyInDatabase.get().setK1(k1.toHex());
            userLnurlAuthKeyRepository.save(linkingKeyInDatabase.get());
        }
        return true;
    }

    @Override
    public Optional<LinkingKey> findPairedLinkingKeyByK1(@NonNull final K1 k1) {
        // This method returns the linking key associated with the k1 passed as parameter.
        logger.info("Finding the paired linking key for k1 {}", k1.toHex());
        final Optional<UserLnurlAuthKey> linkingKey = userLnurlAuthKeyRepository.findByLinkingKey(k1.toHex());
        if (linkingKey.isPresent()) {
            logger.info("Linking key found: {}", linkingKey.get().getLinkingKey());
            return Optional.of(SimpleLinkingKey.fromHex(linkingKey.get().getLinkingKey()));
        } else {
            logger.info("Linking key not found");
            return Optional.empty();
        }
    }

}
