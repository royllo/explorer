package org.royllo.explorer.web.configuration;

import lombok.RequiredArgsConstructor;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.royllo.explorer.core.util.parameters.IncomingRateLimitsParameters;
import org.royllo.explorer.web.util.interceptor.RateLimitInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    /** Assets search results default page size. */
    public static final int ASSET_SEARCH_DEFAULT_PAGE_SIZE = 10;

    /** Asset's proofs default page size. */
    public static final int ASSET_PROOFS_DEFAULT_PAGE_SIZE = 100;

    /** Incoming rate limits parameters. */
    private final IncomingRateLimitsParameters incomingRateLimitsParameters;

    @Override
    @SuppressWarnings("checkstyle:DesignForExtension")
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(incomingRateLimitsParameters));
    }

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
