package org.royllo.explorer.web.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.tbk.lnurl.auth.SimpleK1Manager;

/**
 * Security configuration.
 */
@Controller
@EnableWebSecurity
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension"})
public class SecurityConfiguration {

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

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        //  Each request that comes in passes through this chain of filters before reaching your application.
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                // No authentication required for the public website.
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll());
        return http.build();
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

}
