package avto.accord.App.Domain.Models.Article;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ArticleRequest {
    private String title;
    @NotBlank(message = "Description is mandatory")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
}
