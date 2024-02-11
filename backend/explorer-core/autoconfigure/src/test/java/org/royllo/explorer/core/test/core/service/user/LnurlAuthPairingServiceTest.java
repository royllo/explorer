package org.royllo.explorer.core.test.core.service.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.domain.user.UserLnurlAuthKey;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.repository.user.UserLnurlAuthKeyRepository;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.tbk.lnurl.auth.K1;
import org.tbk.lnurl.auth.LinkingKey;
import org.tbk.lnurl.simple.auth.SimpleK1;
import org.tbk.lnurl.simple.auth.SimpleLinkingKey;
import org.tbk.spring.lnurl.security.userdetails.LnurlAuthUserPairingService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.enums.UserRole.USER;

@SpringBootTest
@DirtiesContext
@DisplayName("LnurlAuthPairingService tests")
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class LnurlAuthPairingServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLnurlAuthKeyRepository userLnurlAuthKeyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LnurlAuthUserPairingService lnurlAuthUserPairingService;

    @Test
    @Disabled
    @DisplayName("pairK1WithLinkingKey() and findPairedLinkingKeyByK1()")
    void lnurlAuthPairingService() {
        // Unknown user login information.
        String simpleK1Value = "e2af6254a8df433264fa23f67eb8188635d15ce883e8fc020989d5f82ae6f11e";
        String anotherK1Value = "d4f067cf72b94baddac1b3856b231669c3239d59cce95ef260da58363fb01822";
        String linkingKey1Value = "02c3b844b8104f0c1b15c507774c9ba7fc609f58f343b9b149122e944dd20c9362";
        K1 k1Test1 = SimpleK1.fromHex(simpleK1Value);
        K1 k1Test2 = SimpleK1.fromHex(anotherK1Value);
        LinkingKey linkingKeyTest1 = SimpleLinkingKey.fromHex(linkingKey1Value);

        // We clean the data.
        userLnurlAuthKeyRepository.findByLinkingKey(linkingKey1Value).ifPresent(userLnurlAuthKey -> userLnurlAuthKeyRepository.delete(userLnurlAuthKey));
        userRepository.findByUsernameIgnoreCase(linkingKey1Value).ifPresent(user -> userRepository.delete(user));

        // We check the data we have.
        final long userCount = userRepository.count();
        final long userLnurlAuthKeyCount = userLnurlAuthKeyRepository.count();

        // =============================================================================================================
        // Test 1 : a new user logs in (it doesn't exist in database).
        assertFalse(userService.getUserByUsername(linkingKey1Value).isPresent());
        assertFalse(userLnurlAuthKeyRepository.findByK1(simpleK1Value).isPresent());

        // We pair & check if the data is here.
        //lnurlAuthUserPairingService.pairUserWithK1(k1Test1, linkingKeyTest1);

        // We check the number of data created.
        assertEquals(userCount + 1, userRepository.count());
        assertEquals(userLnurlAuthKeyCount + 1, userLnurlAuthKeyRepository.count());

        // We check the user created.
        Optional<UserDTO> newUserCreated = userService.getUserByUsername(linkingKey1Value);
        assertTrue(newUserCreated.isPresent());
        assertNotNull(newUserCreated.get().getId());
        assertEquals(linkingKey1Value, newUserCreated.get().getUsername());
        assertEquals(USER, newUserCreated.get().getRole());

        // We check the user lnurl-auth key created.
        Optional<UserLnurlAuthKey> newUserLinkingKeyCreated = userLnurlAuthKeyRepository.findByLinkingKey(linkingKey1Value);
        assertTrue(newUserLinkingKeyCreated.isPresent());
        assertNotNull(newUserLinkingKeyCreated.get().getId());
        assertEquals(newUserCreated.get().getId(), newUserLinkingKeyCreated.get().getOwner().getId());
        assertEquals(simpleK1Value, newUserLinkingKeyCreated.get().getK1());
        assertEquals(linkingKey1Value, newUserLinkingKeyCreated.get().getLinkingKey());
        assertTrue(userLnurlAuthKeyRepository.findByK1(simpleK1Value).isPresent());

        // =============================================================================================================
        // Test 2 : the same user logs again with another k1 - No new data but k1 updated.
        //lnurlAuthUserPairingService.pairK1WithLinkingKey(k1Test2, linkingKeyTest1);
        assertEquals(userCount + 1, userRepository.count());
        assertEquals(userLnurlAuthKeyCount + 1, userLnurlAuthKeyRepository.count());

        // We check the user lnurl-auth key updated.
        newUserLinkingKeyCreated = userLnurlAuthKeyRepository.findByLinkingKey(linkingKey1Value);
        assertTrue(newUserLinkingKeyCreated.isPresent());
        assertNotNull(newUserLinkingKeyCreated.get().getId());
        assertEquals(newUserCreated.get().getId(), newUserLinkingKeyCreated.get().getOwner().getId());
        assertEquals(anotherK1Value, newUserLinkingKeyCreated.get().getK1());
        assertEquals(linkingKey1Value, newUserLinkingKeyCreated.get().getLinkingKey());
    }

}
