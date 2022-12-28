package org.royllo.explorer.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@SuppressWarnings("checkstyle:DesignForExtension")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected final void configure(final HttpSecurity http) throws Exception {
        http.antMatcher("/graphql/**")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests().anyRequest().permitAll();
    }

}
