package avto.accord.App.Domain.Models.Product;

import avto.accord.App.Domain.Models.Price.Price;
import avto.accord.App.Domain.Models.Price.PriceResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response object for Product")
public class ProductResponse {
    private int id;
    private String name;
    private String brand;
    private int count;
    @Schema(description = "цена товара")
    private PriceResponse price;
    private String countType;
    private String description;
    private String article;
    private boolean specialOffer;
    private String customerArticle;
    private String categoryName;
    private String mainPhotoUrl;
    private List<String> additionalPhotos;
    private List<PropertyValue> properties;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PropertyValue {
        private String name;
        private String value;
    }
}
