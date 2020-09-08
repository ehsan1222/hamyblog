package ir.hamyblog.services;

import ir.hamyblog.entities.Article;
import ir.hamyblog.entities.User;
import ir.hamyblog.exceptions.FileException;
import ir.hamyblog.exceptions.FileNotFoundException;
import ir.hamyblog.model.ArticlesListOut;
import ir.hamyblog.repositories.ArticleRepository;
import ir.hamyblog.services.io.StorageService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    public ImageOutput getArticleImage(UUID uid) {
        File file = storageService.get(uid);
        if (file == null) {
            throw new FileNotFoundException("file not found, " + uid);
        }
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return new ImageOutput(new ByteArrayResource(fileInputStream.readAllBytes()), file.getName());
        } catch (IOException e) {
            throw new FileException();
        }
    }

    public Article getArticle(UUID uid) {
        return articleRepository.findById(uid)
                .orElseThrow(
                        () -> new FileNotFoundException("article not found, " + uid)
                );
    }

    @AllArgsConstructor
    @Getter
    public class ImageOutput {
        private Resource resource;
        private String filename;
    }
}
