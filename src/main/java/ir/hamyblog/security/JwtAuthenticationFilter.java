package ir.hamyblog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ir.hamyblog.services.HamyblogUserDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl(SecurityConstants.AUTHENTICATE_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            BufferedReader reader = request.getReader();
            RequestToken requestToken = objectMapper.readValue(reader, RequestToken.class);

            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(requestToken.getUsername(), requestToken.getPassword());
            return this.authenticationManager.authenticate(token);
        } catch (IOException e) {
            throw new AuthenticationCredentialsNotFoundException("Error in reading request body");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        if (authResult.getPrincipal() instanceof HamyblogUserDetails) {
            HamyblogUserDetails principal = (HamyblogUserDetails) authResult.getPrincipal();

            var username = principal.getUsername();
            var authorities = principal.getAuthorities();
            SecretKey secretKey = principal.getSecretKey();

            String token = Jwts.builder()
                    .signWith(secretKey, SignatureAlgorithm.HS512)
                    .setSubject(username)
                    .claim("rol", authorities)
                    .compact();

            try {
                PrintWriter writer = response.getWriter();
                response.addHeader("content-type", "application/json");
                objectMapper.writeValue(writer, SecurityConstants.AUTHENTICATION_PREFIX.concat(token));
            } catch (IOException e) {
                throw new RuntimeException("error in sending token ", e);
            }
        }
    }
}
