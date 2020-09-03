package ir.hamyblog.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Getter
public class User {

    @Id
    @EqualsAndHashCode.Include
    private UUID uuid;

    @Column(unique = true)
    private String username;
    private String password;
    private String fullName;
    private String secretKey;

    public User(String username, String password, String fullName, String secretKey) {
        uuid = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.secretKey = secretKey;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
