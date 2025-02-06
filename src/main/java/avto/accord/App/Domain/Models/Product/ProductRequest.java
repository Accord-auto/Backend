package avto.accord.App.Domain.Models.Product;

import avto.accord.App.Domain.Models.Price.PriceRequest;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String brand;
    private int count;
    private String countType;
    private String description;
    private String article;
    private boolean specialOffer;
    private String customerArticle;
    private PriceRequest price;
    private MultipartFile mainPhoto;
    private List<MultipartFile> additionalPhotos = new ArrayList<>();
    private int categoryId;
    private List<ProductPropertyRequest> properties = new ArrayList<>();
}
