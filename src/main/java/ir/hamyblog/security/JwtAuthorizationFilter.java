package ir.hamyblog.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import ir.hamyblog.entities.User;
import ir.hamyblog.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ir.hamyblog.security.SecurityConstants.AUTHENTICATION_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserService userService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        UsernamePasswordAuthenticationToken token = getAuthorization(request);

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthorization(HttpServletRequest request) {

        String jwtToken = request.getHeader(AUTHORIZATION);
        if (jwtToken != null && jwtToken.startsWith(AUTHENTICATION_PREFIX)) {

            try{
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKeyResolver(new MySigningKeyResolver()).build()
                        .parseClaimsJws(jwtToken.replace(AUTHENTICATION_PREFIX, ""));

                String username = claimsJws.getBody().getSubject();

                Set<SimpleGrantedAuthority> authorities = ((List<?>) claimsJws.getBody().get("rol")).stream()
                        .map(authority -> new SimpleGrantedAuthority(String.valueOf(authority)))
                        .collect(Collectors.toSet());

                if (username != null && !username.isEmpty()) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
                log.warn("user not found {}", username);
            } catch (SignatureException e) {
                log.warn("Request to parse Jwt with invalid signature : \"{}\" failed \"{}\"", jwtToken, e.getMessage());
            }
        }
        return null;
    }

    private class MySigningKeyResolver extends SigningKeyResolverAdapter {
        @Override
        public Key resolveSigningKey(JwsHeader header, Claims claims) {
            String username = claims.getSubject();
            User user = userService.getUserByUsername(username);
            return Keys.hmacShaKeyFor(Base64.getDecoder().decode(user.getSecretKey()));
        }
    }
}
