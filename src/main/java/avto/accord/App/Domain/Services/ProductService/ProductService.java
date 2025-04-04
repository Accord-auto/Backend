package avto.accord.App.Domain.Services.ProductService;

import avto.accord.App.Application.Facade.IProductFacade;
import avto.accord.App.Application.Services.IProductService;
import avto.accord.App.Domain.Models.FilterRequest.ProductFilter;
import avto.accord.App.Domain.Models.Page.CustomPage;
import avto.accord.App.Domain.Models.Price.PriceResponse;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Models.Product.ProductResponse;
import avto.accord.App.Domain.Models.Product.ProductSort;
import avto.accord.App.Domain.Repositories.Product.ProductRepository;
import avto.accord.App.Infrastructure.Components.ProductSpecifications.ProductSpecifications;
import avto.accord.App.Infrastructure.Exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository _productRepository;
    private final IProductFacade _productFacade;


    /**
     * @return
     */
    @Override
    public List<String> getBrands() {
        return _productRepository.findAll().stream().map(Product::getBrand).distinct().toList();
    }

    @Override
    public Optional<Product> findByArticle(String article) {
        return _productRepository.findByArticle(article);
    }

    @Override
    public Optional<Product> findByCustomerArticle(String customerArticle) {
        return _productRepository.findByCustomerArticle(customerArticle);
    }

    @Override
    public CustomPage<Product> filterProducts(
            ProductFilter filter,
            int offset,
            int limit,
            ProductSort sort) {

        Pageable pageable = PageRequest.of(offset, limit, sort.getSortValue());
        Specification<Product> spec = Specification.where(null);

        if (filter.getCategoryIds() != null && !filter.getCategoryIds().isEmpty())
            spec = spec.and(ProductSpecifications.inCategories(filter.getCategoryIds()));

        if (filter.getBrands() != null && !filter.getBrands().isEmpty())
            spec = spec.and(ProductSpecifications.hasBrands(filter.getBrands()));

        if (filter.getMinPrice() != null || filter.getMaxPrice() != null)
            spec = spec.and(ProductSpecifications.hasPriceBetween(
                    filter.getMinPrice(), filter.getMaxPrice()));

        if (filter.getProperties() != null && !filter.getProperties().isEmpty())
            spec = spec.and(ProductSpecifications.hasProperties(filter.getProperties()));

        Page<Product> productPage = _productRepository.findAll(spec, pageable);
        return new CustomPage<>(productPage);
    }


    @Override
    public List<Product> getSpecialOffer() {
        return _productRepository.findRandomSpecialOfferProduct();
    }

    @Override
    public CustomPage<ProductResponse> getAllProducts(int offset, int limit, ProductSort sort) {
        Pageable pageable = PageRequest.of(offset, limit, sort.getSortValue());
        Page<Product> productPage = _productRepository.findAll(pageable);

        return new CustomPage<>(productPage.map(this::getProductResponse));
    }

    @Override
    public ProductResponse getProductById(int productId) {
        log.info("Fetching product with ID: {}", productId);

        Product target = _productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        return getProductResponse(target);
    }


    private ProductResponse getProductResponse(Product target) {
        Map<String, List<String>> propertiesMap = new HashMap<>();
        Optional.ofNullable(target.getProperties())
                .orElse(Collections.emptyList())
                .forEach(productProperty -> {
                    String propertyName = productProperty.getProperty().getName();
                    String propertyValue = productProperty.getValue();
                    propertiesMap.computeIfAbsent(propertyName, k -> new ArrayList<>()).add(propertyValue);
                });
        PriceResponse priceResponse = new PriceResponse(
                target.getPrice().getValue(),
                target.getPrice().getDiscount()
        );

        return new ProductResponse(
                target.getId(),
                target.getName(),
                target.getBrand(),
                target.getCount(),
                priceResponse,
                target.getCountType(),
                target.getDescription(),
                target.getArticle(),
                target.isSpecialOffer(),
                target.getCustomerArticle(),
                target.getCategory().getName(),
                target.getMainPhotoUrl(),
                target.getAdditionalPhotos(),
                propertiesMap
        );
    }

    @Override
    public Product saveProduct(ProductRequest productRequest) throws IOException {
        try {
            Product product = _productFacade.createProduct(productRequest);
            if (product.getAdditionalPhotos() == null) {
                product.setAdditionalPhotos(new ArrayList<>());
            }
            if (product.getProperties() == null) {
                product.setProperties(new ArrayList<>());
            }

            log.info("Saving product: {}", product);
            return _productRepository.save(product);
        } catch (RuntimeException e) {
            log.error("Error saving product: {}", e.getMessage());
            throw new RuntimeException("Error saving product", e);
        }
    }

    public Product saveProduct(Product product) {
        return _productRepository.save(product);
    }

    @Override
    @Transactional
    public boolean deleteProduct(int id) {
        Optional<Product> productOptional = _productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Удаление фотографий, связанных с товаром
            List<String> photosToDelete = new ArrayList<>();
            if (product.getMainPhotoUrl() != null) {
                photosToDelete.add(product.getMainPhotoUrl());
            }
            if (product.getAdditionalPhotos() != null && !product.getAdditionalPhotos().isEmpty()) {
                photosToDelete.addAll(product.getAdditionalPhotos());
            }

            if (!photosToDelete.isEmpty()) {
                _productFacade.deletePhotos(photosToDelete);
            }

            _productRepository.delete(product);
            return true;
        }
        return false;
    }


    @Override
    @Transactional
    public Product updatePrice(int productId, int newPrice) {
        Product product = _productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.getPrice().setValue(newPrice);
        return _productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateDiscount(int productId, int newDiscount) {
        Product product = _productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.getPrice().setDiscount(newDiscount);
        return _productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateCount(int productId, int newCount) {
        Product product = _productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setCount(newCount);
        return _productRepository.save(product);
    }

    @Override
    public Product updateCustomerArticle(int id, String customerArticle) {
        Optional<Product> optionalProduct = _productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setCustomerArticle(customerArticle);
            return _productRepository.save(product);
        }
        log.error("Product with ID {} not found", id);
        return null;
    }

    @Override
    public void toggleSpecialOffer(int id) {
        try {
            _productRepository.toggleSpecialOffer(id);
        } catch (Exception e) {
            log.error("Error toggle special offer", e);
            throw new RuntimeException("Error toggle special offer", e);
        }
    }

}
