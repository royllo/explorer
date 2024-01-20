package org.royllo.explorer.web.configuration;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.tbk.lnurl.auth.InMemoryLnurlAuthPairingService;
import org.tbk.lnurl.auth.K1Manager;
import org.tbk.lnurl.auth.LnurlAuthFactory;
import org.tbk.lnurl.auth.LnurlAuthPairingService;
import org.tbk.lnurl.auth.SimpleK1Manager;
import org.tbk.lnurl.auth.SimpleLnurlAuthFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configuration for LNURL-auth.
 */
@Controller
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension"})
public class LnurlAuthConfiguration {

    /**
     * This factory creates a LNURL-auth callback url for the given k1.
     *
     * @param k1Manager      k1 manager
     * @param servletContext servlet context
     * @return lnurl auth factory
     */
    @Bean
    @SneakyThrows(URISyntaxException.class)
    LnurlAuthFactory lnurlAuthFactory(final K1Manager k1Manager,
                                      final ServletContext servletContext) {
        URI callbackUrl = new URI(servletContext.getContextPath() + "/api/v1/lnurl/auth/callback");
        return new SimpleLnurlAuthFactory(callbackUrl, k1Manager);
    }

    /**
     * This service acts like a user detail service for LNURL-auth.
     * It has two methods to see if a k1 is paired with a linking key and to find a linking key by k1.
     * boolean pairK1WithLinkingKey(K1 k1, LinkingKey linkingKey);
     * Optional<LinkingKey> findPairedLinkingKeyByK1(K1 k1);
     *
     * @return lnurl auth security service
     */
    @Bean
    LnurlAuthPairingService lnurlAuthSecurityService() {
        // TODO Implement this.
        return new InMemoryLnurlAuthPairingService();
    }

    /**
     * K1 manager. "k1" refers to a one-time, randomly generated key or token.
     *
     * @return k1 manager
     */
    @Bean
    SimpleK1Manager k1Manager() {
        return new SimpleK1Manager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // The primary purpose of the UserDetailsService is to load user-specific data.
        // It is used by the AuthenticationManager to authenticate a user during the login process.
        // When a username and password are submitted (e.g., via a login form), Spring Security's AuthenticationManager
        // uses the UserDetailsService to load the user details.
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
                .roles("USER")
                .build());
        return manager;
    }

}
