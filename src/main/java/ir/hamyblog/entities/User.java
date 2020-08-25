package ir.hamyblog.entities;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class User {

    @Id
    @EqualsAndHashCode.Include
    private UUID uuid;

    @Column(unique = true)
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        uuid = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }
}
