package ru.clevertec.newsManagement.security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expired}")
    private String validityInMilliseconds;

    private final UserDetailsService userDetailsService;

    @Lazy
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    /**
     * Создание токена на основе ролей пользователей и времени истечения срока действия
     *
     * @param username имя пользователя
     * @param roles    роли
     * @return токен
     */
    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + Integer.parseInt(validityInMilliseconds));
        log.debug("Генерирует токен на основе пользователя, ролей и времени истечения срока действия");
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String resolverToken(HttpServletRequest req) {
        String authorization = req.getHeader("Authorization");
        String substring = null;
        if (authorization != null) {
            substring = authorization.substring(7);
        }
        return substring;
    }

    /**
     * аутентификация на основе токена
     *
     * @param token токен
     * @return аутентификация
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserName(token));
        if (userDetails == null) {
            throw new UsernameNotFoundException("username not found");
        }
        log.debug("Аутентификация на основе токена");
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Имя пользователя по токену
     *
     * @param token токен
     * @return имя пользователя
     */
    public String getUserName(String token) {
        log.debug("Username by token");
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Проверяет срок действия токена
     *
     * @param token token
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            log.debug("Check token validity - expiration date");
            if (!claims.getBody().getExpiration().before(new Date())) {
                return true;
            }
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("token not valid");
            throw new AccessDeniedException("token not valid");
        }
    }

}
