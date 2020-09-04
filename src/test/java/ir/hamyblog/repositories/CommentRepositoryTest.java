package ir.hamyblog.repositories;

import ir.hamyblog.entities.Article;
import ir.hamyblog.entities.Comment;
import ir.hamyblog.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
public class CommentRepositoryTest {

    @Autowired
    private TestEntityManager manager;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void shouldRemoveCommentAfterRemoveArticle(){
        Article article = new Article("titie", UUID.randomUUID(), "content");
        User user = new User("usernmae", "password", "ehsan maddahi", "");
        manager.persist(article);
        manager.persist(user);

        Comment comment = new Comment("content comment", user, article);
        article.newComment(comment);
        manager.persist(article);

        manager.remove(article);

        assertThat(commentRepository.findAll()).isEmpty();
    }

    @Test
    public void shouldChangeLastModifiedDateAfterCommentChanged() {
        Article article = new Article("titie", UUID.randomUUID(), "content");
        User user = new User("usernmae", "password", "ehsan maddahi", "");
        manager.persist(article);
        manager.persist(user);

        Comment comment = new Comment("content comment", user, article);
        article.newComment(comment);
        manager.persist(article);

        final Optional<Comment> commentOptional = commentRepository.findById(comment.getUuid());
        System.out.println(commentOptional.get().getLastModified());
    }


}