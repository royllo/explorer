package org.royllo.explorer.api.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Security configuration.
 */
@Controller
@EnableWebSecurity
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension"})
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        //  Each request that comes in passes through this chain of filters before reaching your application.
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(NEVER))
                // Page authorisations.
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(antMatcher("/**")).permitAll()
                ).build();
    }

}
