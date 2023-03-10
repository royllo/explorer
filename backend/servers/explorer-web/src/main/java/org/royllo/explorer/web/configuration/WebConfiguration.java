package org.royllo.explorer.web.configuration;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Web configuration.
 */
@Configuration
public class WebConfiguration {

    /** Asset search results default page size. */
    public static final int ASSET_SEARCH_DEFAULT_PAGE_SIZE = 10;

    /** Asset proofs default page size. */
    public static final int ASSET_PROOFS_DEFAULT_PAGE_SIZE = 100;

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
