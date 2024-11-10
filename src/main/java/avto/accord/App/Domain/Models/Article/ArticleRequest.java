package avto.accord.App.Domain.Models.Article;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ArticleRequest {
    private String title;
    private MultipartFile photo;
    private String description;
}
