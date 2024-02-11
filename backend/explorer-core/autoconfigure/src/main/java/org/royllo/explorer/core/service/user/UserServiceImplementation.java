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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.tbk.lnurl.auth.K1;
import org.tbk.lnurl.auth.SignedLnurlAuth;
import org.tbk.spring.lnurl.security.userdetails.LnurlAuthUserPairingService;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.royllo.explorer.core.util.constants.AdministratorUserConstants.ADMINISTRATOR_USER_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.enums.UserRole.USER;

/**
 * {@link UserService} implementation.
 * Also implements {@link LnurlAuthUserPairingService} that brings the wallet and the web session together.
 */
@Service
@Validated
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class UserServiceImplementation extends BaseService implements UserService, LnurlAuthUserPairingService, UserDetailsService {

    /** User repository. */
    private final UserRepository userRepository;

    /** User lnurl-auth key repository. */
    private final UserLnurlAuthKeyRepository userLnurlAuthKeyRepository;

    @Override
    public UserDTO getAdministratorUser() {
        logger.info("Getting administrator user");

        final Optional<UserDTO> administratorUser = getUserByUserId(ADMINISTRATOR_USER_ID);
        if (administratorUser.isPresent()) {
            logger.info("Returning administrator user");
            return administratorUser.get();
        } else {
            logger.error("Administrator user not found - This should never happened");
            throw new RuntimeException("Administrator user not found");
        }
    }

    @Override
    public UserDTO getAnonymousUser() {
        logger.info("Getting anonymous user");

        final Optional<UserDTO> anonymousUser = getUserByUserId(ANONYMOUS_USER_ID);
        if (anonymousUser.isPresent()) {
            logger.info("Returning anonymous user");
            return anonymousUser.get();
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
    public void updateUser(final String username, final UserDTO userData) {
        logger.info("Update a user with username: {}, new value is {}", username, userData);
        Optional<User> user = userRepository.findByUsernameIgnoreCase(username);

        // Verification.
        assert user.isPresent() : "User not found with username: " + username;

        // We update the data.
        user.get().setFullName(userData.getFullName());
        user.get().setBiography(userData.getBiography());
        user.get().setWebsite(userData.getWebsite());
        userRepository.save(user.get());
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
    public UserDetails pairUserWithK1(final SignedLnurlAuth auth) {
        // This method is called by the spring boot starter when a user has provided a k1 signed with a linking key and everything is valid.
        // Usually, this is where you search if the linking key already exists in the database.
        // If not, you create a new user and store the linking key.
        // If yes, you just return the user, and you update the k1 used.
        final Optional<UserLnurlAuthKey> linkingKeyInDatabase = userLnurlAuthKeyRepository.findByLinkingKey(auth.getLinkingKey().toHex());
        if (linkingKeyInDatabase.isEmpty()) {
            logger.info("Creating the user for the linking key: {}", auth.getLinkingKey().toHex());
            // We create the user.
            final UserDTO user = createUser(auth.getLinkingKey().toHex());
            // We create the user lnurl-auth key.
            userLnurlAuthKeyRepository.save(UserLnurlAuthKey.builder()
                    .owner(USER_MAPPER.mapToUser(user))
                    .linkingKey(auth.getLinkingKey().toHex())
                    .k1(auth.getK1().toHex())
                    .build());

            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    UUID.randomUUID().toString(),
                    Collections.singletonList((new SimpleGrantedAuthority(user.getRole().toString()))));
        } else {
            logger.info("User with the linking key {} exists", auth.getLinkingKey().toHex());
            linkingKeyInDatabase.get().setK1(auth.getK1().toHex());
            userLnurlAuthKeyRepository.save(linkingKeyInDatabase.get());

            return new org.springframework.security.core.userdetails.User(linkingKeyInDatabase.get().getOwner().getUsername(),
                    UUID.randomUUID().toString(),
                    linkingKeyInDatabase.get().getOwner().getRole().asGrantedAuthorityList());
        }
    }

    @Override
    public Optional<UserDetails> findPairedUserByK1(final K1 k1) {
        // This method returns the linking key associated with the k1 passed as parameter.
        logger.info("Finding the paired linking key for k1 {}", k1.toHex());
        final Optional<UserLnurlAuthKey> linkingKey = userLnurlAuthKeyRepository.findByK1(k1.toHex());
        if (linkingKey.isPresent()) {
            logger.info("Linking key found: {}", linkingKey.get().getLinkingKey());
            return Optional.of(new org.springframework.security.core.userdetails.User(linkingKey.get().getOwner().getUsername(),
                    UUID.randomUUID().toString(),
                    linkingKey.get().getOwner().getRole().asGrantedAuthorityList()));
        } else {
            logger.info("Linking key NOT found: {}", k1.toHex());
            return Optional.empty();
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        // Search for the user with it's linking key.
        final UserLnurlAuthKey userLinkingKey = userLnurlAuthKeyRepository
                .findByLinkingKey(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(userLinkingKey.getOwner().getUsername(),
                UUID.randomUUID().toString(),
                userLinkingKey.getOwner().getRole().asGrantedAuthorityList());
    }

}
