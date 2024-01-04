package org.royllo.explorer.web.configuration;

import lombok.RequiredArgsConstructor;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.jetbrains.annotations.NotNull;
import org.royllo.explorer.core.util.parameters.IncomingRateLimitsParameters;
import org.royllo.explorer.web.util.interceptor.RateLimitInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static java.util.concurrent.TimeUnit.DAYS;

/**
 * Web configuration.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {


    /** Assets search results default page size. */
    public static final int ASSET_SEARCH_DEFAULT_PAGE_SIZE = 10;

    /** Asset group assets default page size. */
    public static final int ASSET_GROUP_ASSETS_DEFAULT_PAGE_SIZE = 10;

    /** Asset states default page size. */
    public static final int ASSET_STATES_DEFAULT_PAGE_SIZE = 10;

    /** Asset's proofs default page size. */
    public static final int ASSET_PROOFS_DEFAULT_PAGE_SIZE = 10;

    /** Default cache duration. */
    public static final CacheControl DEFAULT_CACHE_DURATION = CacheControl.maxAge(1, DAYS);

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
                "classpath:/i18n/search",
                "classpath:/i18n/statistic"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Override
    @SuppressWarnings("checkstyle:DesignForExtension")
    public void addResourceHandlers(@NotNull final ResourceHandlerRegistry registry) {
        // Cache Images
        registry.addResourceHandler("/images/favicon/**")
                .addResourceLocations("classpath:/static/images/favicon/")
                .setCacheControl(DEFAULT_CACHE_DURATION);

        registry.addResourceHandler("/images/logo/**")
                .addResourceLocations("classpath:/static/images/logo/")
                .setCacheControl(DEFAULT_CACHE_DURATION);

        registry.addResourceHandler("/images/open_graph/**")
                .addResourceLocations("classpath:/static/images/open_graph/")
                .setCacheControl(DEFAULT_CACHE_DURATION);

        // Cache SVG
        registry.addResourceHandler("/svg/footer/**")
                .addResourceLocations("classpath:/static/svg/footer/")
                .setCacheControl(DEFAULT_CACHE_DURATION);

        registry.addResourceHandler("/svg/type_*.svg")
                .addResourceLocations("classpath:/static/svg/")
                .setCacheControl(DEFAULT_CACHE_DURATION);
    }

}
