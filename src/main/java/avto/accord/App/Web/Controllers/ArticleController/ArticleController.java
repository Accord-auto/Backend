package avto.accord.App.Web.Controllers.ArticleController;

import avto.accord.App.Domain.Models.Article.Article;
import avto.accord.App.Domain.Models.Article.ArticleRequest;
import avto.accord.App.Domain.Models.Product.ProductRequestPayload;
import avto.accord.App.Domain.Services.ArticleService.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private ObjectMapper objectMapper;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> createArticle(
            @Parameter(description = "Article request payload", required = true, schema = @Schema(implementation = ArticleRequest.class))
            @RequestPart("articleRequestPayload") String articleRequestPayloadJson,
            @RequestPart("Photo") MultipartFile photo
    ) {
        try {
            ArticleRequest articleRequest = objectMapper.readValue(articleRequestPayloadJson, ArticleRequest.class);
            Article createdArticle = articleService.createArticle(articleRequest, photo);
            return ResponseEntity.ok(createdArticle);
        } catch (Exception e) {
            log.error("Error creating article", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> updateArticle(
            @PathVariable int id,
            @Parameter(description = "Article request payload", schema = @Schema(implementation = ArticleRequest.class))
            @RequestPart("articleRequestPayload") String articleRequestPayloadJson,
            @RequestPart("Photo") MultipartFile newPhoto
    ) {
        try {
            ArticleRequest articleRequest = objectMapper.readValue(articleRequestPayloadJson, ArticleRequest.class);
            Article updatedArticle = articleService.updateArticle(id, articleRequest, newPhoto);
            return ResponseEntity.ok(updatedArticle);
        } catch (Exception e) {
            log.error("Error updating article", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable int id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
