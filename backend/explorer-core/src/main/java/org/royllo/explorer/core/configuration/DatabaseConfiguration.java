package org.royllo.explorer.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Database configuration.
 * TODO Is this necessary with Spring boot 3.0 ?
 */
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class DatabaseConfiguration {

    /**
     * Makes ZonedDateTime compatible with auditing fields.
     *
     * @return DateTimeProvider
     */
    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }

}