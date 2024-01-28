package org.royllo.explorer.core.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Caching configuration.
 */
@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CachingConfiguration {

}
