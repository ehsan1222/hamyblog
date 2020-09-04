package ir.hamyblog.services;

import io.jsonwebtoken.security.Keys;
import ir.hamyblog.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HamyblogUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;
    private final SecretKey secretKey;

    public HamyblogUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = new HashSet<>();
        this.authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        String base64EncodedSecretKey = user.getSecretKey();
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64EncodedSecretKey.getBytes()));
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
