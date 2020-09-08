package ir.hamyblog.web;

import ir.hamyblog.entities.Article;
import ir.hamyblog.model.ArticlesListOut;
import ir.hamyblog.services.ArticleService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

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
    public ResponseEntity<ArticlesListOut> getArticles(@RequestParam("page") Integer page) {
        ArticlesListOut articlesListOut = articleService.getArticlesByPageNumber(page);
        return new ResponseEntity<>(articlesListOut, HttpStatus.OK);
    }

    @GetMapping("/articles/image/{uid}")
    public ResponseEntity<Resource> getArticleImage(@PathVariable("uid") UUID uid) throws IOException {
        ArticleService.ImageOutput imageOutput = articleService.getArticleImage(uid);
        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + imageOutput.getFilename() + "\"")
                .body(imageOutput.getResource());
    }
}
