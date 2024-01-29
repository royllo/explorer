package org.royllo.explorer.web.configuration;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class OtherLnurlAuthConfiguration {

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
        URI callbackUrl = new URI("https://3d28-2001-861-5300-9e20-3a75-b2e0-d13a-27f5.ngrok-free.app" + servletContext.getContextPath() + LNURL_AUTH_WALLET_LOGIN_PATH);
        return new SimpleLnurlAuthFactory(callbackUrl, k1Manager);
    }

}
