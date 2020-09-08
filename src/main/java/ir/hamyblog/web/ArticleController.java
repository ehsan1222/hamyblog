package ir.hamyblog.web;

import ir.hamyblog.entities.Article;
import ir.hamyblog.model.ArticlesListOut;
import ir.hamyblog.services.ArticleService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@Validated
@RequestMapping(
        path = "/articles"
)
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<Article> addArticle(@RequestParam("title") @NonNull String title,
                                              @RequestParam("content") @NonNull String content,
                                              @RequestBody @NonNull MultipartFile pic,
                                              Authentication authentication) {
        String username = authentication.getName();
        Article article = articleService.addArticle(username, title, content, pic);
        return new ResponseEntity<>(article, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ArticlesListOut> getArticles(@RequestParam("page") Integer page) {
        ArticlesListOut articlesListOut = articleService.getArticlesByPageNumber(page);
        return new ResponseEntity<>(articlesListOut, HttpStatus.OK);
    }

    @GetMapping("/image/{uid}")
    public ResponseEntity<Resource> getArticleImage(@PathVariable("uid") UUID uid) {
        ArticleService.ImageOutput imageOutput = articleService.getArticleImage(uid);
        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + imageOutput.getFilename() + "\"")
                .body(imageOutput.getResource());
    }

    @GetMapping("/{uid}")
    public ResponseEntity<Article> getArticle(@PathVariable("uid") UUID uid) {
        Article article = articleService.getArticle(uid);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }
}
