package ir.hamyblog.services;

import ir.hamyblog.entities.Article;
import ir.hamyblog.entities.User;
import ir.hamyblog.repositories.ArticleRepository;
import ir.hamyblog.services.io.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final StorageService storageService;
    private final UserService userService;

    public ArticleService(ArticleRepository articleRepository,
                          StorageService storageService,
                          UserService userService) {
        this.articleRepository = articleRepository;
        this.storageService = storageService;
        this.userService = userService;
    }

    public Article addArticle(String username, String title, String content, MultipartFile pic) {
        User user = userService.getUserByUsername(username);
        UUID imageUid = storageService.store(pic);
        Article article = new Article(title, imageUid, content, user);
        return articleRepository.save(article);
    }
}
