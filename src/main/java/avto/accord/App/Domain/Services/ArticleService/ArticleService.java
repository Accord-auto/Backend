package avto.accord.App.Domain.Services.ArticleService;

import avto.accord.App.Application.Services.IArticleService;
import avto.accord.App.Domain.Models.Article.Article;
import avto.accord.App.Domain.Models.Article.ArticleRequest;

import avto.accord.App.Domain.Repositories.Article.ArticleRepository;
import avto.accord.App.Infrastructure.Components.Photos.PhotoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService implements IArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private PhotoUtils photoUtils;

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Optional<Article> getArticleById(int id) {
        return articleRepository.findById(id);
    }

    @Override
    public Article createArticle(ArticleRequest articleRequest, MultipartFile photo) throws IOException {
        Article article = new Article();
        article.setTitle(articleRequest.getTitle());
        article.setDescription(articleRequest.getDescription());
        article.setPhotoUrl(savePhoto(photo)); // Метод для сохранения фото
        return articleRepository.save(article);
    }

    @Override
    public Article updateArticle(int id, ArticleRequest articleRequest, MultipartFile newPhoto) throws IOException {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.setTitle(articleRequest.getTitle());
            article.setDescription(articleRequest.getDescription());
            article.setPhotoUrl(savePhoto(newPhoto)); // Метод для сохранения фото
            return articleRepository.save(article);
        }
        return null;
    }

    @Override
    public void deleteArticle(int id) {
        articleRepository.deleteById(id);
    }
    private String savePhoto(MultipartFile photo) throws IOException {
        return photoUtils.savePhoto(photo);
    }
}
