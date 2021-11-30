package de.fevkav.mooddetection.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Dieser Filter fängt alle Requests auf Endpunkte mit erforderlicher Authentifizierung ab. Der abgefangene Request wird
 * schließlich überprüft, ob es einen validen JWT beinhaltet und ob der auch einem Benutzer zugeordnet werden kann.
 */

public class JWTFilter extends GenericFilterBean {

    public final static String HEADER_STRING = "Authorization";

    private  final TokenService tokenService;

    public JWTFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Prüft, ob der Token valide ist.
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String jwt = resolveToken(httpServletRequest);

        if (jwt != null) {
            Authentication authentication = this.tokenService.getAuthentication(jwt);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Prüft, ob der Header des Requests der Form "Authorization:Bearer xxx...x" entspricht und extrahiert den Token.
     * @param request Der abgefangene Request auf ein Endpunkt mit erforderlicher Authentifizierung.
     * @return
     */
    private static String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(HEADER_STRING);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;


    }


}
