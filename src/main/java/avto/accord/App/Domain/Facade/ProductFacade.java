package avto.accord.App.Domain.Facade;

import avto.accord.App.Application.Facade.IProductFacade;
import avto.accord.App.Application.Services.ICategoryService;
import avto.accord.App.Application.Services.IProductService;
import avto.accord.App.Application.Services.IPropertyService;
import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Price.Price;
import avto.accord.App.Domain.Models.Price.PriceRequest;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Infrastructure.Components.Photos.PhotoUtils;
import avto.accord.App.Domain.Services.PriceService.PriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductFacade implements IProductFacade {
    private final ICategoryService categoryService;
    private final PhotoUtils photoUtils;
    private final IPropertyService propertyService;
    private final PriceService priceService;
    @Lazy
    @Autowired
    private IProductService productService;

    @Override
    public Product createProduct(ProductRequest productRequest) throws IOException {
        log.info("Creating product with request: {}", productRequest);

        validateProductRequest(productRequest);

        Product product = mapToProduct(productRequest);
        setPhotos(productRequest, product);
        setCategory(productRequest, product);
        setProperties(productRequest, product);

        // Сохранение продукта
        Product savedProduct = productService.saveProduct(product);
        setPrice(productRequest.getPrice(), savedProduct);

        log.info("Product created successfully: {}", savedProduct);
        return savedProduct;
    }

    private void setPhotos(ProductRequest productRequest, Product product) throws IOException {
        if (productRequest.getMainPhoto() != null) {
            String mainPhotoPath = savePhoto(productRequest.getMainPhoto());
            product.setMainPhotoUrl(mainPhotoPath);
        }

        if (productRequest.getAdditionalPhotos() != null) {
            List<String> additionalPhotoPaths = saveAdditionalPhotos(productRequest.getAdditionalPhotos());
            product.setAdditionalPhotos(additionalPhotoPaths);
        }
    }

    private void setCategory(ProductRequest productRequest, Product product) {
        Category category = categoryService.getCategoryById(productRequest.getCategoryId());
        product.setCategory(category);
    }

    private void setProperties(ProductRequest productRequest, Product product) {
        List<ProductProperty> properties = mapProperties(productRequest.getProperties(), product);
        product.setProperties(properties);
    }

    private void setPrice(PriceRequest priceRequest, Product savedProduct) {
        Price price = new Price();
        price.setValue(priceRequest.getValue());
        price.setDiscount(priceRequest.getDiscount());
        price.setProduct(savedProduct);

        try {
            priceService.savePrice(price);
            savedProduct.setPrice(price);  // Устанавливаем цену после успешного сохранения
        } catch (Exception e) {
            log.error("Error saving price for product {}: {}", savedProduct.getId(), e.getMessage());
            throw new RuntimeException("Failed to save price", e);
        }
    }

    private List<ProductProperty> mapProperties(List<ProductPropertyRequest> propertyRequests, Product product) {
        if (propertyRequests == null) {
            return new ArrayList<>();
        }

        return propertyRequests.stream()
                .map(propertyRequest -> mapPropertyRequestToProductProperty(propertyRequest, product))
                .collect(Collectors.toList());
    }

    private ProductProperty mapPropertyRequestToProductProperty(ProductPropertyRequest propertyRequest, Product product) {
        Property property = propertyService.getPropertyByIdOnly(propertyRequest.getPropertyId());

        // Проверяем существование значения свойства
        ProductProperty existingProperty = propertyService.findProductPropertyByPropertyIdAndValue(
                property.getId(),
                propertyRequest.getValue()
        );

        if (existingProperty != null) {
            existingProperty.setProduct(product); // Связываем с текущим продуктом
            return existingProperty;
        }

        // Если значение не найдено, создаем новое
        ProductProperty newProperty = new ProductProperty();
        newProperty.setValue(propertyRequest.getValue());
        newProperty.setProperty(property);
        newProperty.setProduct(product);

        // Сохраняем новое свойство в базе данных
        return newProperty;
    }

    private String savePhoto(MultipartFile photo) throws IOException {
        return photoUtils.savePhoto(photo);
    }

    private List<String> saveAdditionalPhotos(List<MultipartFile> additionalPhotos) throws IOException {
        List<String> additionalPhotoPaths = new ArrayList<>();

        for (MultipartFile photo : additionalPhotos) {
            additionalPhotoPaths.add(savePhoto(photo));
        }

        return additionalPhotoPaths;
    }
    private Product mapToProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setBrand(productRequest.getBrand());
        product.setCount(productRequest.getCount());
        product.setCountType(productRequest.getCountType());
        product.setDescription(productRequest.getDescription());
        product.setArticle(productRequest.getArticle());
        product.setSpecialOffer(productRequest.isSpecialOffer());
        product.setCustomerArticle(productRequest.getCustomerArticle());
        product.setAdditionalPhotos(new ArrayList<>());
        product.setProperties(new ArrayList<>());

        Price price = new Price();
        price.setValue(productRequest.getPrice().getValue());
        price.setDiscount(productRequest.getPrice().getDiscount());
        price.setProduct(product);
        product.setPrice(price);

        return product;
    }

    private void validateProductRequest(ProductRequest productRequest) {
        if (productRequest.getName() == null || productRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }

        if (productRequest.getBrand() == null || productRequest.getBrand().isEmpty()) {
            throw new IllegalArgumentException("Product brand cannot be null or empty");
        }

        if (productRequest.getCount() <= 0) {
            throw new IllegalArgumentException("Product count must be greater than zero");
        }

        if (productRequest.getCategoryId() <= 0) {
            throw new IllegalArgumentException("Product category ID must be greater than zero");
        }

        if (productRequest.getPrice() == null) {
            throw new IllegalArgumentException("Product price cannot be null");
        }
    }
}
