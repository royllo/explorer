package org.royllo.explorer.web.configuration;

import lombok.RequiredArgsConstructor;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.royllo.explorer.core.util.parameters.IncomingRateLimitsParameters;
import org.royllo.explorer.web.util.interceptor.RateLimitInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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

    /** Asset group assets default page size. */
    public static final int ASSET_GROUP_ASSETS_DEFAULT_PAGE_SIZE = 100;

    /** Asset states default page size. */
    public static final int ASSET_STATES_DEFAULT_PAGE_SIZE = 100;

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

    /**
     * Message source for i18n.
     *
     * @return message source
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:/i18n/asset",
                "classpath:/i18n/html",
                "classpath:/i18n/request",
                "classpath:/i18n/search"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
