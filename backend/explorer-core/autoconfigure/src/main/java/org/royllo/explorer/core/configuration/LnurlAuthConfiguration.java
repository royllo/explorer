package org.royllo.explorer.core.configuration;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.repository.util.K1ValueRepository;
import org.royllo.explorer.core.service.util.DatabaseK1Manager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tbk.lnurl.auth.K1Manager;

/**
 * LNURL Auth configuration.
 */
@Configuration
@RequiredArgsConstructor
public class LnurlAuthConfiguration {

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

}
