package org.royllo.explorer.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebSecurity
@SuppressWarnings("checkstyle:DesignForExtension")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

    @Override
    protected final void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/graphql").permitAll()
                .antMatchers("/graphiql").permitAll()
                .antMatchers("/vendor/**").permitAll()
                .anyRequest().authenticated();
    }

}
