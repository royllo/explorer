package org.royllo.explorer.core.service.user;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.domain.user.UserLnurlAuthKey;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.dto.user.UserDTOSettings;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.user.UserLnurlAuthKeyRepository;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.validator.Username;
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

    /** Asset repository. */
    private final AssetRepository assetRepository;

    @Override
    public UserDTO addUser(final @Username String username) {
        logger.info("Creating a user with username: {}", username);

        // =============================================================================================================
        // Checking constraints.

        // The username should not be used already.
        if (userRepository.findByUsernameIgnoreCase(username.trim()).isPresent()) {
            throw new IllegalArgumentException("Username '" + username + "' already registered");
        }

        // =============================================================================================================
        // Creating the user.

        final User userCreated = userRepository.save(User.builder()
                .userId(UUID.randomUUID().toString())
                .username(username.trim().toLowerCase())
                .role(USER)
                .build());

        logger.info("User created: {}", userCreated);
        return USER_MAPPER.mapToUserDTO(userCreated);
    }

    @Override
    public void updateUser(final String userId, final @Valid UserDTOSettings userSettings) {
        logger.info("Update a user with username: {}, with those settings {}", userId, userSettings);

        // =============================================================================================================
        // Checking constraints.

        // We search the user to update.
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with userId: " + userId));

        // If the username in the updated settings are different from the current username.
        if (!user.getUsername().equalsIgnoreCase(userSettings.getCleanedUsername())) {
            // We check that the new username is not already used.
            userRepository.findByUsernameIgnoreCase(userSettings.getCleanedUsername()).ifPresent(userFound -> {
                throw new IllegalArgumentException("New username '" + userSettings.username() + "' already registered");
            });
        }

        // =============================================================================================================
        // We update the data.
        user.setProfilePictureFileName(userSettings.profilePictureFileName());
        user.setUsername(userSettings.username());
        user.setFullName(userSettings.fullName());
        user.setBiography(userSettings.biography());
        user.setWebsite(userSettings.website());
        userRepository.save(user);
    }

    @Override
    public Optional<UserDTO> getUserByUserId(@NonNull final String userId) {
        logger.info("Getting user with userId: {}", userId);

        return userRepository.findByUserId(userId)
                .map(USER_MAPPER::mapToUserDTO)
                .map(user -> {
                    logger.info("User with userId '{}' found: {}", userId, user);
                    return user;
                })
                .or(() -> {
                    logger.info("User with userId {} not found", userId);
                    return Optional.empty();
                });
    }

    @Override
    public Optional<UserDTO> getUserByUsername(final String username) {
        logger.info("Getting user with username: {}", username);

        return userRepository.findByUsernameIgnoreCase(username)
                .map(USER_MAPPER::mapToUserDTO)
                .map(user -> {
                    logger.info("User with username '{}' found: {}", username, user);
                    return user;
                })
                .or(() -> {
                    logger.info("User with username {} not found", username);
                    return Optional.empty();
                });
    }

    @Override
    public UserDetails pairUserWithK1(final SignedLnurlAuth auth) {
        // This method is called by the spring boot starter when a user has provided a k1 signed with a linking key and everything is valid.
        // Usually, this is where you search if the linking key already exists in the database.
        // If not, you create a new user and store the linking key.
        // If yes, you just return the user, and you update the k1 used.
        return userLnurlAuthKeyRepository
                .findByLinkingKey(auth.getLinkingKey().toHex())
                .map(userLnurlAuthKey -> {
                    logger.info("User with the linking key {} exists", auth.getLinkingKey().toHex());
                    userLnurlAuthKey.setK1(auth.getK1().toHex());
                    userLnurlAuthKeyRepository.save(userLnurlAuthKey);

                    return new org.springframework.security.core.userdetails.User(userLnurlAuthKey.getOwner().getUserId(),
                            UUID.randomUUID().toString(),
                            userLnurlAuthKey.getOwner().getRole().asGrantedAuthorityList());
                })
                .orElseGet(() -> {
                    logger.info("Creating the user for the linking key: {}", auth.getLinkingKey().toHex());
                    final UserDTO user = addUser(auth.getLinkingKey().toHex());
                    userLnurlAuthKeyRepository.save(UserLnurlAuthKey.builder()
                            .owner(USER_MAPPER.mapToUser(user))
                            .linkingKey(auth.getLinkingKey().toHex())
                            .k1(auth.getK1().toHex())
                            .build());

                    return new org.springframework.security.core.userdetails.User(user.getUserId(),
                            UUID.randomUUID().toString(),
                            Collections.singletonList((new SimpleGrantedAuthority(user.getRole().toString()))));
                });
    }

    @Override
    public Optional<UserDetails> findPairedUserByK1(final K1 k1) {
        // This method returns the linking key associated with the k1 passed as parameter.
        logger.info("Finding the paired linking key for k1 {}", k1.toHex());

        return Optional.ofNullable(userLnurlAuthKeyRepository.findByK1(k1.toHex())
                .map(userLnurlAuthKey -> {
                    logger.info("Linking key found: {}", userLnurlAuthKey.getLinkingKey());
                    return new org.springframework.security.core.userdetails.User(userLnurlAuthKey.getOwner().getUserId(),
                            UUID.randomUUID().toString(),
                            userLnurlAuthKey.getOwner().getRole().asGrantedAuthorityList());
                })
                .orElseGet(() -> {
                    logger.info("Linking key NOT found: {}", k1.toHex());
                    return null;
                }));
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        // Search for the user with it's linking key.
        logger.info("Loading the username {}", username);

        return userLnurlAuthKeyRepository
                .findByLinkingKey(username)
                .map(userLinkingKey -> new org.springframework.security.core.userdetails.User(
                        userLinkingKey.getOwner().getUserId(),
                        UUID.randomUUID().toString(),
                        userLinkingKey.getOwner().getRole().asGrantedAuthorityList()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

}
