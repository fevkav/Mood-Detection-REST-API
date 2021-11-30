package de.fevkav.mooddetection.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final TokenService tokenService;

    public WebSecurityConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable().cors()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic()
                .and().authorizeRequests()

                .antMatchers("/register").permitAll()
                .antMatchers("/managers").permitAll()
                .antMatchers("/login").permitAll()

                .antMatchers("/authemployee").authenticated()
                .antMatchers("/authmanager").authenticated()
                .antMatchers("/currentweek").authenticated()
                .antMatchers("/managers/employees").authenticated()
                .antMatchers("/votes/avg").authenticated()
                .antMatchers("/votes/amount").authenticated()
                .antMatchers("/voting").authenticated()
                .antMatchers("/votestatus").authenticated()

                .anyRequest().denyAll()
                .and()
                .apply(new JWTConfigurer(this.tokenService));

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
    }



}
