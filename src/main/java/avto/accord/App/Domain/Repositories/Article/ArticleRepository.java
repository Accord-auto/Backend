package avto.accord.App.Domain.Repositories.Article;

import avto.accord.App.Domain.Models.Article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
