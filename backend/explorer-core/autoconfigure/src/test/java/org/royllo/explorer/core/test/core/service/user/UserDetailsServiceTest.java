package org.royllo.explorer.core.test.core.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.tbk.lnurl.auth.K1;
import org.tbk.lnurl.auth.LinkingKey;
import org.tbk.lnurl.auth.Signature;
import org.tbk.lnurl.auth.SignedLnurlAuth;
import org.tbk.lnurl.simple.auth.SimpleK1;
import org.tbk.lnurl.simple.auth.SimpleLinkingKey;
import org.tbk.lnurl.simple.auth.SimpleLnurlAuth;
import org.tbk.lnurl.simple.auth.SimpleSignature;
import org.tbk.lnurl.simple.auth.SimpleSignedLnurlAuth;
import org.tbk.spring.lnurl.security.userdetails.LnurlAuthUserPairingService;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext
@DisplayName("UserDetailsService tests")
public class UserDetailsServiceTest {

    @Autowired
    private LnurlAuthUserPairingService lnurlAuthPairingService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("loadUserByUsername()")
    void loadUserByUsername() throws URISyntaxException {
        // New user.
        String user1K1Value = "e2af6254a8df433264fa23f67eb8188635d15ce883e8fc020989d5f82ae6f11e";
        String user1linkingKey1Value = "02c3b844b8104f0c1b15c507774c9ba7fc609f58f343b9b149122e944dd20c9362";
        K1 user1K1Test1 = SimpleK1.fromHex(user1K1Value);
        LinkingKey user1LinkingKeyTest1 = SimpleLinkingKey.fromHex(user1linkingKey1Value);

        // We create a new user.
        SimpleLnurlAuth simpleLnurlAuth = SimpleLnurlAuth.create(new URI("http://locahost"), user1K1Test1);
        Signature signature = SimpleSignature.fromHex("3046022100cd9f98eb6cda6d0b5f479a1709baed16d8e09e49a697d1f2a5315b933b29ff65022100983e4c03206656c67d849c142d55d7c6c80ec70b87a01bbffa6fb71464381b4e");
        SignedLnurlAuth user1SignedLnurlAuth = SimpleSignedLnurlAuth.create(simpleLnurlAuth, user1LinkingKeyTest1, signature);
        lnurlAuthPairingService.pairUserWithK1(user1SignedLnurlAuth);

        // We try the load the user created.
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user1linkingKey1Value);
        assertNotNull(userDetails);
        assertEquals(user1linkingKey1Value, userDetails.getUsername());

        // We try to load a non-existing user.
        UsernameNotFoundException e = assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("NON_EXISTING_USERNAME"));
        assertEquals("User not found with username: NON_EXISTING_USERNAME", e.getMessage());
    }

}
