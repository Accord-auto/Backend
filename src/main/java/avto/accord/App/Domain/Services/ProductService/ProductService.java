package avto.accord.App.Domain.Services.ProductService;

import avto.accord.App.Application.Factory.IProductFactory;
import avto.accord.App.Application.Services.IProductService;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Models.Product.ProductSort;
import avto.accord.App.Domain.Repositories.Product.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository _productRepository;
    private final IProductFactory _productFactory;

    @Override
    public Page<Product> getAllProducts(int offset, int limit, ProductSort sort) {
        Pageable pageable = PageRequest.of(offset, limit, sort.getSortValue());
        return _productRepository.findAll(pageable);
    }

    @Override
    public Product getProductById(int productId) {
        return _productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Override
    public Product saveProduct(ProductRequest productRequest) throws IOException {
        try {
            Product product = _productFactory.createProduct(productRequest);
            return _productRepository.save(product);
        } catch (RuntimeException e) {
            log.error("Error saving product: {}", e.getMessage(), e);
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
            _productRepository.delete(productOptional.get());
            return true;
        }
        return false;
    }


    @Override
    @Transactional
    public Product updatePrice(int productId, int newPrice) {
        Product product = getProductById(productId);
        product.getPrice().setValue(newPrice);
        return _productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateDiscount(int productId, int newDiscount) {
        Product product = getProductById(productId);
        product.getPrice().setDiscount(newDiscount);
        return _productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateCount(int productId, int newCount) {
        Product product = getProductById(productId);
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
}
