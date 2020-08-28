package ir.hamyblog.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
    private User user;

    @ManyToOne
    private Article article;

    @CreatedDate
    private Date lastModified;

    public Comment(String content, User user, Article article) {
        this.uuid = UUID.randomUUID();
        this.content = content;
        this.user = user;
        this.article = article;
    }
}
