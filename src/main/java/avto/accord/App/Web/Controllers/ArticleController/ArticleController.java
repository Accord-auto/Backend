package avto.accord.App.Web.Controllers.ArticleController;

import avto.accord.App.Domain.Models.Article.Article;
import avto.accord.App.Domain.Models.Article.ArticleRequest;
import avto.accord.App.Domain.Services.ArticleService.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "статьи")
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable int id) {
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Article> createArticle(@ModelAttribute ArticleRequest articleRequest) throws IOException {
        Article createdArticle = articleService.createArticle(articleRequest);
        return ResponseEntity.ok(createdArticle);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Article> updateArticle(@PathVariable int id, @ModelAttribute ArticleRequest articleRequest) throws IOException {
        Article updatedArticle = articleService.updateArticle(id, articleRequest);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable int id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
