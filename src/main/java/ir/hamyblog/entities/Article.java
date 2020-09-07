package ir.hamyblog.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
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

    @JsonFormat(timezone = "Asia/Tehran", pattern = "yyyy-MM-dd HH:mm")
    private Date creationDate;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JsonIgnoreProperties({"uuid", "role"})
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private final List<Comment> comments = new ArrayList<>();

    public Article(String title, UUID imageUid, String content, User user) {
        this.uuid = UUID.randomUUID();
        this.title = title;
        this.imageUid = imageUid;
        this.content = content;
        this.user = user;
        this.creationDate = new Date();
    }

    public void newComment(Comment comment) {
        this.comments.add(comment);
    }

}
