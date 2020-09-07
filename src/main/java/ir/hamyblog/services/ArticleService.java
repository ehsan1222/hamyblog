package ir.hamyblog.services;

import ir.hamyblog.entities.Article;
import ir.hamyblog.entities.User;
import ir.hamyblog.model.ArticlesListOut;
import ir.hamyblog.repositories.ArticleRepository;
import ir.hamyblog.services.io.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    public static final int PAGE_SIZE = 10;
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

    public ArticlesListOut getArticlesByPageNumber(int pageNumber) {
        int pageNumberInPageable = pageNumber - 1;
        Page<Article> articlePage = articleRepository
                .findAll(PageRequest.of(
                        pageNumberInPageable,
                        PAGE_SIZE,
                        Sort.by("creationDate").descending())
                );
        List<Article> articles = articlePage.get().collect(Collectors.toList());
        return new ArticlesListOut(articlePage.getTotalPages(), pageNumber, articles);
    }
}
