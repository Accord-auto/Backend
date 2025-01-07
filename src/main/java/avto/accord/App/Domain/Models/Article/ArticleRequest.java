package avto.accord.App.Domain.Models.Article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequest {
    private String title;
    @NotBlank(message = "Description is mandatory")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
}
