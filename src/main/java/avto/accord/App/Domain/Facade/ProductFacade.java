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
import avto.accord.App.Infrastructure.Components.Mapper.ProductPropertyMapper;
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
    @Autowired
    private ICategoryService _categoryService;

    @Autowired
    private PhotoUtils photoUtils;

    @Autowired
    private IPropertyService _propertyService;

    @Autowired
    private PriceService _priceService;

    @Autowired
    @Lazy
    private IProductService productService;

    @Override
    public Product createProduct(ProductRequest productRequest) throws IOException {
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
        Category category = _categoryService.getCategoryById(productRequest.getCategoryId());
        product.setCategory(category);

        // Установка свойств
        product.setProperties(mapProperties(productRequest.getProperties(), product));

        // Сохранение продукта
        Product savedProduct = productService.saveProduct(product);

        // Установка цены
        Price price = createPrice(productRequest.getPrice(), savedProduct);
        savedProduct.setPrice(price);

        return savedProduct;
    }

    private Product mapToProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setBrand(productRequest.getBrand());
        product.setCount(productRequest.getCount());
        product.setCountType(productRequest.getCountType());
        product.setDescription(productRequest.getDescription());
        product.setArticle(productRequest.getArticle());
        return product;
    }

    private List<String> saveAdditionalPhotos(List<MultipartFile> additionalPhotos) throws IOException {
        List<String> additionalPhotoPaths = new ArrayList<>();
        for (MultipartFile additionalPhoto : additionalPhotos) {
            additionalPhotoPaths.add(savePhoto(additionalPhoto));
        }
        return additionalPhotoPaths;
    }

    private List<ProductProperty> mapProperties(List<ProductPropertyRequest> propertyRequests, Product product) {
        return propertyRequests.stream()
                .map(propertyRequest -> mapPropertyRequestToProductProperty(propertyRequest, product))
                .collect(Collectors.toList());
    }

    private ProductProperty mapPropertyRequestToProductProperty(ProductPropertyRequest propertyRequest, Product product) {
        ProductProperty productProperty = ProductPropertyMapper.INSTANCE.toProductProperty(propertyRequest);

        Property property = _propertyService.getPropertyByIdOnly(propertyRequest.getPropertyId());
        if (property == null) {
            throw new IllegalArgumentException("Property not found");
        }

        productProperty.setProperty(property);
        productProperty.setProduct(product);
        return productProperty;
    }

    private String savePhoto(MultipartFile photo) throws IOException {
        return photoUtils.savePhoto(photo);
    }

    private Price createPrice(PriceRequest priceRequest, Product product) {
        Price price = new Price();
        price.setProduct(product);
        price.setDiscount(priceRequest.getDiscount());
        price.setValue(priceRequest.getValue());
        _priceService.savePrice(price);
        return price;
    }
}
