package avto.accord.App.Domain.Models.Product;

import avto.accord.App.Domain.Models.Price.PriceRequest;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductRequest {
    private String name;
    private String brand;
    private int count;
    private PriceRequest price;
    private String countType;
    private String description;
    private String article;
    private MultipartFile mainPhoto;
    private List<MultipartFile> additionalPhotos = new ArrayList<>();
    private int categoryId;
    private List<ProductPropertyRequest> properties = new ArrayList<>();
    private boolean specialOffer;
    private String customerArticle;
}
