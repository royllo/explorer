package org.royllo.explorer.web.configuration;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.royllo.explorer.core.repository.util.K1ValueRepository;
import org.royllo.explorer.core.service.util.DatabaseK1Manager;
import org.royllo.explorer.core.util.parameters.RoylloExplorerParameters;
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
public class LnurlAuthConfiguration {

    /** Royllo explorer parameters. */
    private final RoylloExplorerParameters roylloExplorerParameters;

    /** K1 value repository. */
    private final K1ValueRepository k1ValueRepository;

    /**
     * K1 manager (managed thanks to a database table).
     * "k1" refers to a one-time, randomly generated key or token.
     *
     * @return k1 manager
     */
    @Bean
    K1Manager k1Manager() {
        return new DatabaseK1Manager(k1ValueRepository);
    }

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
        URI callbackUrl = new URI(roylloExplorerParameters.getWeb().getBaseUrl() + servletContext.getContextPath() + LNURL_AUTH_WALLET_LOGIN_PATH);
        return new SimpleLnurlAuthFactory(callbackUrl, k1Manager);
    }

}
