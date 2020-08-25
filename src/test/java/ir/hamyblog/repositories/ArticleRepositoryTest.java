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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ArticleRepositoryTest {

    @Autowired
    private TestEntityManager manager;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void shouldReturnSavedArticle() {
        final Article article = new Article("test", UUID.randomUUID(), "this is content");

        assertThat(articleRepository.findAll()).isEmpty();
        articleRepository.save(article);

        final Optional<Article> articleOptional = articleRepository.findById(article.getUuid());

        assertThat(articleOptional.isPresent()).isTrue();

        final Article actualArticle = articleOptional.get();
        assertThat(actualArticle.getUuid()).isEqualTo(article.getUuid());
        assertThat(actualArticle.getTitle()).isEqualTo(article.getTitle());
        assertThat(actualArticle.getContent()).isEqualTo(article.getContent());
        assertThat(actualArticle.getImageUid()).isEqualTo(article.getImageUid());
    }

    @Test
    public void shouldSaveNewComment() {
        final Article article = new Article("test", UUID.randomUUID(), "this is content");
        User user = new User("ehsan", "passowrd", "ehsan maddahi");
        manager.persist(article);
        manager.persist(user);

        Comment comment = new Comment("this is good", user, article);
        article.newComment(comment);

        articleRepository.save(article);

        final Article savedArticle = manager.find(Article.class, article.getUuid());
        assertThat(savedArticle).isNotNull();
        assertThat(savedArticle.getTitle()).isEqualTo(article.getTitle());
        assertThat(savedArticle.getImageUid()).isEqualTo(article.getImageUid());
        assertThat(savedArticle.getContent()).isEqualTo(article.getContent());
        assertThat(savedArticle.getComments()).hasSize(1).contains(comment);
        assertThat(savedArticle.getComments().get(0).getUser()).isEqualTo(user);
    }


}