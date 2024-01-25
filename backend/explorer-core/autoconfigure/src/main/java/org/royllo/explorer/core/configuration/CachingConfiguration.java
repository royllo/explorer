package org.royllo.explorer.core.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tbk.lnurl.auth.K1Manager;
import org.tbk.lnurl.auth.SimpleK1Manager;

/**
 * Caching configuration.
 */
@Configuration
@EnableCaching
public class CachingConfiguration {

    /**
     * K1 manager. "k1" refers to a one-time, randomly generated key or token.
     *
     * @return k1 manager
     */
    @Bean
    K1Manager k1Manager() {
        // TODO Suppress when https://github.com/theborakompanioni/bitcoin-spring-boot-starter/issues/110 is fixed.
        return new SimpleK1Manager();
    }

}
