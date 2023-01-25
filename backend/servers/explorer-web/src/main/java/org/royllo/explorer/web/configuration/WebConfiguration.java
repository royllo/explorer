package org.royllo.explorer.web.configuration;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Web configuration.
 */
@Configuration
public class WebConfiguration {

    /** Default page size. */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * Add Thymeleaf Layout Dialect support.
     *
     * @return Thymeleaf template engine
     */
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

}
