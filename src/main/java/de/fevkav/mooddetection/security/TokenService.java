package de.fevkav.mooddetection.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Diese Klasse stellt Methoden zur JWT-Generierung, -Parsen und -Validierung zur Verfügung.
 */
@Component
public class TokenService {

    private static final String SECRETKEY = "InsertSecretKeyOfMTAG";

    private static final long MANAGEREXPIRATIONTIME = 86_400_000;  // ein Tag
    private static final long EMPLOYEEEXPIRATIONTIME = 86_400_000 * 365;  // ein Tag * 365 = ein Jahr

    private final UserDetailsService userDetailsService;

    public TokenService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Generiert einen Token für den Mitarbeiter. Setzt die Gültigkeit und signiert den Token
     * mit einem private key (SECRETKEY).
     * @return
     */
    public String createToken(String username, boolean isManager) {



        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + ((isManager) ? MANAGEREXPIRATIONTIME : EMPLOYEEEXPIRATIONTIME)))
                .signWith(SignatureAlgorithm.HS512, SECRETKEY)
                .compact();
    }

    public Authentication getAuthentication(String token) {

        String username = Jwts.parser()
                .setSigningKey(SECRETKEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        System.out.println("Logged in user: " + username);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());

    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRETKEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }





}
