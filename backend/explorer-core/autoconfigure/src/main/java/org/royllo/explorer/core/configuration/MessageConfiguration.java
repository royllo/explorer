package org.royllo.explorer.core.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Message configuration.
 */
@Configuration
public class MessageConfiguration {

    /**
     * Message source for i18n.
     *
     * @return message source
     */
    @Bean
    public MessageSource errorMessages() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:/i18n/errors"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
