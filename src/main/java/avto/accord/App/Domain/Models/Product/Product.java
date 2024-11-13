package avto.accord.App.Domain.Models.Product;


import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Price.Price;
import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String brand;
    private int count;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "price_id", referencedColumnName = "id")
    @JsonManagedReference
    private Price price;

    private String countType;
    private String description;
    private String article;
    private boolean specialOffer; // спецпредложение
    private String customerArticle; // Уникальный артикул заказчика

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    private String mainPhotoUrl;

    @ElementCollection
    @CollectionTable(name = "additional_photos", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "photo_path")
    @Size(max = 3)
    private List<String> additionalPhotos;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductProperty> properties;

    public Product(int id, String name, String brand, int count, String countType, String description, String article, String mainPhotoUrl, List<String> additionalPhotos, Category category, Price price, List<ProductProperty> properties) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.count = count;
        this.countType = countType;
        this.description = description;
        this.article = article;
        this.mainPhotoUrl = mainPhotoUrl;
        this.additionalPhotos = additionalPhotos;
        this.category = category;
        this.price = price;
        this.properties = properties;
    }
}