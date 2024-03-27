package org.royllo.explorer.api.configuration;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.interceptor.RateLimitInterceptor;
import org.royllo.explorer.core.util.parameters.IncomingRateLimitsParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Web configuration.
 */
@Configuration
@EnableWebMvc
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class WebConfiguration implements WebMvcConfigurer {

    /** Incoming rate limits parameters. */
    private final IncomingRateLimitsParameters incomingRateLimitsParameters;

    @Override

    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(incomingRateLimitsParameters));
    }

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

    @Override
    public final void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**");
    }

}
