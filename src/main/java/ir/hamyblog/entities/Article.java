package ir.hamyblog.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Article {

    @Id
    @EqualsAndHashCode.Include
    private UUID uuid;
    private String title;
    private UUID imageUid;

    @Lob
    private String content;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    public Article(String title, UUID imageUid, String content) {
        this.uuid = UUID.randomUUID();
        this.title = title;
        this.imageUid = imageUid;
        this.content = content;
    }

    public void newComment(Comment comment) {
        this.comments.add(comment);
    }

}
