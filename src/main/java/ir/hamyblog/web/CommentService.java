package ir.hamyblog.web;

import ir.hamyblog.entities.Article;
import ir.hamyblog.entities.Comment;
import ir.hamyblog.entities.User;
import ir.hamyblog.repositories.CommentRepository;
import ir.hamyblog.services.ArticleService;
import ir.hamyblog.services.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository,
                          ArticleService articleService,
                          UserService userService) {
        this.commentRepository = commentRepository;
        this.articleService = articleService;
        this.userService = userService;
    }

    public Comment addComment(UUID articleUid, String username, String content) {
        User user = userService.getUserByUsername(username);
        Article article = articleService.getArticle(articleUid);

        Comment comment = new Comment(content, user, article);

        articleService.addComment(article, comment);
        return comment;
    }

}
