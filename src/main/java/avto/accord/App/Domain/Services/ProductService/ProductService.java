package avto.accord.App.Domain.Services.ProductService;

import avto.accord.App.Application.Services.IProductService;
import avto.accord.App.Domain.Factory.ProductFactory;
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
import jakarta.transaction.Transactional;
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
    private final ProductRepository _productRepository;
    private final ProductFactory _productFactory;

    @Override
    public Page<Product> getAllProducts(int offset, int limit) {
        return _productRepository.findAll(PageRequest.of(offset, limit));
    }
    @Override
    public Product getProductById(int productId) {
        return _productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Override
    @Transactional
    public Product saveProduct(ProductRequest productRequest) throws IOException {
        Product product = _productFactory.createProduct(productRequest);
        return _productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(int productId) {
        Product product = getProductById(productId);
        _productRepository.delete(product);
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
}
