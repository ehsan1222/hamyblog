package ir.hamyblog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.hamyblog.security.JwtAuthenticationFilter;
import ir.hamyblog.security.JwtAuthorizationFilter;
import ir.hamyblog.services.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class HamyblogSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final UserService userService;

    public HamyblogSecurityConfig(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(objectMapper, authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userService))
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
