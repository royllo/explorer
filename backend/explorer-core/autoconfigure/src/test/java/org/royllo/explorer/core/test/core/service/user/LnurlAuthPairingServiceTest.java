package org.royllo.explorer.core.test.core.service.user;

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
import org.tbk.lnurl.auth.LnurlAuthPairingService;
import org.tbk.lnurl.simple.auth.SimpleK1;
import org.tbk.lnurl.simple.auth.SimpleLinkingKey;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private LnurlAuthPairingService lnurlAuthPairingService;

    @Test
    @DisplayName("pairK1WithLinkingKey()")
    void pairK1WithLinkingKey() {
        // We check the data we have.
        final long userCount = userRepository.count();
        final long userLnurlAuthKeyCount = userLnurlAuthKeyRepository.count();

        // Unknown user.
        String SimpleK1Value = "e2af6254a8df433264fa23f67eb8188635d15ce883e8fc020989d5f82ae6f11e";
        String linkingKey1Value = "02c3b844b8104f0c1b15c507774c9ba7fc609f58f343b9b149122e944dd20c9362";
        K1 k1Test1 = SimpleK1.fromHex(SimpleK1Value);
        LinkingKey linkingKeyTest1 = SimpleLinkingKey.fromHex(linkingKey1Value);

        // =============================================================================================================
        // Test 1 : a new user logs in (it doesn't exist in database).
        assertFalse(userService.getUserByUsername(linkingKey1Value).isPresent());

        // We pair & check if the data is here.
        lnurlAuthPairingService.pairK1WithLinkingKey(k1Test1, linkingKeyTest1);

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
        final Optional<UserLnurlAuthKey> newUserLinkingKeyCreated = userLnurlAuthKeyRepository.findByLinkingKey(linkingKey1Value);
        assertTrue(newUserLinkingKeyCreated.isPresent());
        assertNotNull(newUserLinkingKeyCreated.get().getId());
        assertEquals(newUserCreated.get().getId(), newUserLinkingKeyCreated.get().getOwner().getId());
        assertEquals(SimpleK1Value, newUserLinkingKeyCreated.get().getK1());
        assertEquals(linkingKey1Value, newUserLinkingKeyCreated.get().getLinkingKey());

        // =============================================================================================================
        // Test 2 : the same user logs again.

    }

    @Test
    @DisplayName("findPairedLinkingKeyByK1()")
    void findPairedLinkingKeyByK1() {

    }

}
