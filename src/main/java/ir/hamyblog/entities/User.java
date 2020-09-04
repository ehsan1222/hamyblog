package ir.hamyblog.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.hamyblog.model.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Getter
@Table(name = "\"User\"")
public class User {

    @Id
    @EqualsAndHashCode.Include
    private UUID uuid;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;
    private String fullName;

    @JsonIgnore
    private String secretKey;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String password, String fullName, String secretKey) {
        uuid = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.secretKey = secretKey;
        this.role = Role.USER;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
