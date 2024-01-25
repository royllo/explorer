package org.royllo.explorer.web.configuration;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.tbk.lnurl.auth.K1Manager;
import org.tbk.lnurl.auth.LnurlAuthFactory;
import org.tbk.lnurl.auth.SimpleLnurlAuthFactory;

import java.net.URI;
import java.net.URISyntaxException;

import static org.royllo.explorer.web.util.constants.AuthenticationPageConstants.LNURL_AUTH_WALLET_LOGIN_PATH;

/**
 * Configuration for LNURL-auth.
 */
@Configuration
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
        // TODO This should be the url of the explorer-web server.
        URI callbackUrl = new URI("https://dd37-2001-861-5300-9e20-306c-27e-9058-e459.ngrok-free.app" + servletContext.getContextPath() + LNURL_AUTH_WALLET_LOGIN_PATH);
        return new SimpleLnurlAuthFactory(callbackUrl, k1Manager);
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
