package de.fevkav.mooddetection.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Dies ist eine Helper-Klasse, welcher den JWTFilter in die Kette der Sicherheitsfiltern einfügt. Es wird vor dem
 * UsernamePasswordAuthenticationFilter eingefügt, um zu prüfen ob der Request einen validen JWT hat, bevor der
 * Login-Dialog presentiert wird.
 */
public class JWTConfigurer
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenService tokenService;

    public JWTConfigurer(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JWTFilter customFilter = new JWTFilter(this.tokenService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
