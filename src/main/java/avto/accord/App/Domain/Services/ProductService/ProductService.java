package avto.accord.App.Domain.Services.ProductService;

import avto.accord.App.Application.Services.IProductService;
import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Price.Price;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Models.Property.PropertyRequest;
import avto.accord.App.Domain.Repositories.Product.ProductRepository;
import avto.accord.App.Domain.Services.CategoryService.CategoryService;
import avto.accord.App.Domain.Services.PhotoService.PhotoService;
import avto.accord.App.Domain.Services.PriceService.PriceService;
import avto.accord.App.Domain.Services.PropertyService.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository _productRepository;
    @Autowired
    private CategoryService _categoryService;
    @Autowired
    private PhotoService _photoService;
    @Autowired
    private PropertyService _propertyService;
    @Autowired
    private PriceService _priceService;

    @Override
    public Page<Product> getAllProducts(int offset, int limit) {
        return _productRepository.findAll(PageRequest.of(offset, limit));
    }

    /**
     * @param id
     * @return Product
     */
    @Override
    public Product getProduct(int id) {
        return _productRepository.findById(id).orElse(null);
    }

    /**
     * @param productRequest
     * @return Product
     */
    @Override
    public Product saveProduct(ProductRequest productRequest) throws IOException {
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
        Price price = new Price();
        price.setProduct(product);
        price.setDiscount(product.getPrice().getDiscount());
        price.setValue(product.getPrice().getValue());
        _priceService.savePrice(price);
        product.setPrice(price);

        // Установка свойств
        product.setProperties(mapProperties(productRequest.getProperties(), product));

        return _productRepository.save(product);
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
            String additionalPhotoPath = savePhoto(additionalPhoto);
            additionalPhotoPaths.add(additionalPhotoPath);
        }
        return additionalPhotoPaths;
    }

    private List<ProductProperty> mapProperties(List<ProductPropertyRequest> propertyRequests, Product product) {
        List<ProductProperty> productProperties = new ArrayList<>();
        for (ProductPropertyRequest propertyRequest : propertyRequests) {
            ProductProperty productProperty = new ProductProperty();
            productProperty.setValue(propertyRequest.getValue());
            Property property = new Property();
            property.setId(propertyRequest.getPropertyId());
            productProperty.setProperty(property);
            productProperty.setProduct(product);
            productProperties.add(productProperty);
        }
        return productProperties;
    }

    private String savePhoto(MultipartFile photo) throws IOException {
        String photoPath = Paths.get(photo.getOriginalFilename()).getFileName().toString();
        _photoService.savePhoto(photoPath, photo.getBytes());
        return photoPath;
    }

    public Product getProductById(int id) {
        log.info("осуществлён запрос на товар по id: ", id);
        return _productRepository.findById(id).orElse(null);
    }
}
