package org.royllo.explorer.web.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.tbk.lnurl.auth.K1Manager;
import org.tbk.lnurl.auth.LnurlAuthFactory;
import org.tbk.spring.lnurl.security.LnurlAuthConfigurer;
import org.tbk.spring.lnurl.security.userdetails.LnurlAuthUserPairingService;

import static org.royllo.explorer.web.util.constants.AuthenticationPageConstants.LNURL_AUTH_LOGIN_PAGE_PATH;
import static org.royllo.explorer.web.util.constants.AuthenticationPageConstants.LNURL_AUTH_SESSION_K1_KEY;
import static org.royllo.explorer.web.util.constants.AuthenticationPageConstants.LNURL_AUTH_SESSION_LOGIN_PATH;
import static org.royllo.explorer.web.util.constants.AuthenticationPageConstants.LNURL_AUTH_WALLET_LOGIN_PATH;
import static org.royllo.explorer.web.util.constants.UtilPagesConstants.ERROR_403_PAGE;
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

    /** LNURL Auth factory. */
    private final LnurlAuthFactory lnurlAuthFactory;

    /** K1 manager. "k1" refers to a one-time, randomly generated key or token. */
    private final K1Manager lnurlAuthk1Manager;

    /** This service acts like a user detail service for LNURL-auth. */
    private final LnurlAuthUserPairingService lnurlAuthUserPairingService;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        //  Each request that comes in passes through this chain of filters before reaching your application.
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                // Specifying that the application should not create new HTTP sessions on its own, but can use existing ones if they are present.
                // Additionally, it protects against session fixation attacks by migrating the session (i.e., changing the session ID) upon authentication.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(NEVER)
                        .sessionFixation().migrateSession()
                )
                // Page authorisations.
                .authorizeHttpRequests(authorize -> authorize
                        // User account pages (Requires authorizations).
                        .requestMatchers(antMatcher("/account/**"))
                        .authenticated()
                        // Public pages (Permit all).
                        .anyRequest()
                        .permitAll()
                )
                // If the user is not authenticated when accessing a protected page, redirect to the login page.
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .accessDeniedPage(ERROR_403_PAGE)
                        .authenticationEntryPoint((request, response, authenticationException) -> response.sendRedirect(LNURL_AUTH_LOGIN_PAGE_PATH))
                )
                // LNURL Auth configuration.
                .with(new LnurlAuthConfigurer(), lnurlAuthConfigurer ->
                        lnurlAuthConfigurer
                                // Specify the services we built.
                                .k1Manager(lnurlAuthk1Manager)
                                .pairingService(lnurlAuthUserPairingService)
                                .lnurlAuthFactory(lnurlAuthFactory)
                                // Set the login page endpoint.
                                .loginPageEndpoint(login -> login
                                        .enable(true)
                                        .baseUri(LNURL_AUTH_LOGIN_PAGE_PATH)
                                )
                                // Configures the LNURL Authorization Server's Session Endpoint.
                                .sessionEndpoint(session -> session
                                        .baseUri(LNURL_AUTH_SESSION_LOGIN_PATH)
                                        .sessionK1Key(LNURL_AUTH_SESSION_K1_KEY)
                                        .successHandlerCustomizer(successHandler -> {
                                            successHandler.setDefaultTargetUrl("/");
                                            successHandler.setTargetUrlParameter("redirect");
                                            successHandler.setAlwaysUseDefaultTargetUrl(false);
                                            successHandler.setUseReferer(false);
                                        })
                                )
                                // Configures the LNURL Authorization Server's Wallet Endpoint.
                                .walletEndpoint(wallet -> wallet.baseUri(LNURL_AUTH_WALLET_LOGIN_PATH))
                )
                // Logout configuration.
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/"))
                .build();
    }

}
