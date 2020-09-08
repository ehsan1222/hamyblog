package ir.hamyblog.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {

    @Id
    @EqualsAndHashCode.Include
    private UUID uuid;

    private String content;

    @ManyToOne
    @JsonIgnoreProperties({"uuid", "username", "role"})
    private User user;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Article article;

    @JsonFormat(timezone = "Asia/Tehran", pattern = "yyyy-MM-dd HH:mm")
    private Date creationDate;

    public Comment(String content, User user, Article article) {
        this.uuid = UUID.randomUUID();
        this.content = content;
        this.user = user;
        this.article = article;
        this.creationDate = new Date();
    }
}
