    package avto.accord.App.Domain.Models.Product;

    import avto.accord.App.Domain.Models.Price.PriceRequest;
    import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
    import lombok.*;

    import java.util.ArrayList;
    import java.util.List;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProductRequestPayload {
        private String name;
        private String brand;
        private int count;
        private PriceRequest price;
        private String countType;
        private String description;
        private String article;
        private int categoryId;
        private boolean specialOffer;
        private String customerArticle;
        private List<ProductPropertyRequest> properties = new ArrayList<>();
    }
