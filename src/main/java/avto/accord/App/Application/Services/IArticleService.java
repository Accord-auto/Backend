package avto.accord.App.Application.Services;

import avto.accord.App.Domain.Models.Article.Article;
import avto.accord.App.Domain.Models.Article.ArticleRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IArticleService {
    List<Article> getAllArticles();
    Optional<Article> getArticleById(int id);
    Article createArticle(ArticleRequest articleRequest, MultipartFile photo) throws IOException;
    Article updateArticle(int id, ArticleRequest articleRequest, MultipartFile newPhoto) throws IOException;
    void deleteArticle(int id);
}
