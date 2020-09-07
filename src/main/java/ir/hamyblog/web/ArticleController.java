package ir.hamyblog.web;

import ir.hamyblog.entities.Article;
import ir.hamyblog.model.ArticlesOut;
import ir.hamyblog.services.ArticleService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Validated
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    public ResponseEntity<Article> addArticle(@RequestParam("title") @NonNull String title,
                                              @RequestParam("content") @NonNull String content,
                                              @RequestBody @NonNull MultipartFile pic,
                                              Authentication authentication) {
        String username = authentication.getName();
        Article article = articleService.addArticle(username, title, content, pic);
        return new ResponseEntity<>(article, HttpStatus.CREATED);
    }

    @GetMapping("/articles")
    public ResponseEntity<ArticlesOut> getArticles(@RequestParam("page") Integer page) {
        ArticlesOut articlesOut = articleService.getArticlesByPageNumber(page);
        return new ResponseEntity<>(articlesOut, HttpStatus.OK);
    }
}
