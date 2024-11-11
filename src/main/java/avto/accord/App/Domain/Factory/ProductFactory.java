package avto.accord.App.Domain.Factory;

import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Price.Price;
import avto.accord.App.Domain.Models.Price.PriceRequest;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Services.CategoryService.CategoryService;
import avto.accord.App.Domain.Services.PhotoService.PhotoService;
import avto.accord.App.Domain.Services.PriceService.PriceService;
import avto.accord.App.Domain.Services.PropertyService.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductFactory {
    @Autowired
    private CategoryService _categoryService;

    @Autowired
    private PhotoService _photoService;

    @Autowired
    private PropertyService _propertyService;

    @Autowired
    private PriceService _priceService;

    public Product createProduct(ProductRequest productRequest) throws IOException {
        try {
            Product product = mapToProduct(productRequest);

            // Сохранение главного фото
            if (productRequest.getMainPhoto() != null) {
                String mainPhotoPath = savePhoto(productRequest.getMainPhoto());
                product.setMainPhotoUrl(mainPhotoPath);
            }

            // Сохранение дополнительных фото
            if (productRequest.getAdditionalPhotos() != null) {
                List<String> additionalPhotoPaths = saveAdditionalPhotos(productRequest.getAdditionalPhotos());
                product.setAdditionalPhotos(additionalPhotoPaths);
            }

            // Установка категории
            Category category = _categoryService.getCategoryById(productRequest.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            product.setCategory(category);

            // Установка цены
            Price price = createPrice(productRequest.getPrice(), product);
            product.setPrice(price);

            // Установка свойств
            product.setProperties(mapProperties(productRequest.getProperties(), product));

            return product;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Product mapToProduct(ProductRequest productRequest) {
        Product product = new Product();
        try {
            product.setName(productRequest.getName());
            product.setBrand(productRequest.getBrand());
            product.setCount(productRequest.getCount());
            product.setCountType(productRequest.getCountType());
            product.setDescription(productRequest.getDescription());
            product.setArticle(productRequest.getArticle());
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<String> saveAdditionalPhotos(List<MultipartFile> additionalPhotos) throws IOException {
        List<String> additionalPhotoPaths = new ArrayList<>();
        try {
            for (MultipartFile additionalPhoto : additionalPhotos) {
                String additionalPhotoPath = savePhoto(additionalPhoto);
                additionalPhotoPaths.add(additionalPhotoPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return additionalPhotoPaths;
    }

    // ! очень страшный метод😡
    // ? но нужно ли его оптимизировать
    // TODO: обдумать полезность оптимизации метода
    private List<ProductProperty> mapProperties(List<ProductPropertyRequest> propertyRequests, Product product) {
        List<ProductProperty> productProperties = new ArrayList<>();
        for (ProductPropertyRequest propertyRequest : propertyRequests) {
            ProductProperty productProperty = new ProductProperty();
            productProperty.setValue(propertyRequest.getValue());
            Property property = _propertyService.getPropertyById(propertyRequest.getPropertyId());
            if (property == null) {
                throw new IllegalArgumentException("Property not found");
            }
            productProperty.setProperty(property);
            productProperty.setProduct(product);
            productProperties.add(productProperty);
        }
        return productProperties;
    }

    private String savePhoto(MultipartFile photo) throws IOException {
        try {
            String photoPath = Paths.get(photo.getOriginalFilename()).getFileName().toString();
            _photoService.savePhoto(photoPath, photo.getBytes());
            return photoPath;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Price createPrice(PriceRequest priceRequest, Product product) {
        Price price = new Price();
        try {
            price.setProduct(product);
            price.setDiscount(priceRequest.getDiscount());
            price.setValue(priceRequest.getValue());
            _priceService.savePrice(price);
            return price;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
