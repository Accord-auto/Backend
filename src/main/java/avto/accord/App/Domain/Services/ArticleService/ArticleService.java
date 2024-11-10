package avto.accord.App.Domain.Services.ArticleService;

import avto.accord.App.Domain.Models.Article.Article;
import avto.accord.App.Domain.Models.Article.ArticleRequest;
import avto.accord.App.Domain.Repositories.Article.ArticleRepository;
import avto.accord.App.Domain.Services.PhotoService.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private PhotoService photoService;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(int id) {
        return articleRepository.findById(id);
    }

    public Article createArticle(ArticleRequest articleRequest) throws IOException {
        Article article = new Article();
        article.setTitle(articleRequest.getTitle());
        article.setDescription(articleRequest.getDescription());

        if (articleRequest.getPhoto() != null) {
            String photoPath = savePhoto(articleRequest.getPhoto());
            article.setPhotoUrl(photoPath);
        }

        return articleRepository.save(article);
    }

    public Article updateArticle(int id, ArticleRequest articleRequest) throws IOException {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
        article.setTitle(articleRequest.getTitle());
        article.setDescription(articleRequest.getDescription());

        if (articleRequest.getPhoto() != null) {
            String photoPath = savePhoto(articleRequest.getPhoto());
            article.setPhotoUrl(photoPath);
        }

        return articleRepository.save(article);
    }

    public void deleteArticle(int id) {
        articleRepository.deleteById(id);
    }

    private String savePhoto(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        photoService.savePhoto(fileName, file.getBytes());
        return fileName;
    }
}
