package ir.hamyblog.model;

import ir.hamyblog.entities.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ArticlesListOut {
    private int totalPages;
    private int currentPageNumber;
    private List<Article> articles;
}
