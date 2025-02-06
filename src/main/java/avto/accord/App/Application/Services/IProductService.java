package avto.accord.App.Application.Services;

import avto.accord.App.Domain.Models.Page.CustomPage;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Models.Product.ProductResponse;
import avto.accord.App.Domain.Models.Product.ProductSort;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProductService {
    Optional<Product> findByArticle(String article);
    Optional<Product> findByCustomerArticle(String customerArticle);
    CustomPage<Product> filterProducts(int categoryId, Map<String, List<String>> properties, BigDecimal minPrice, BigDecimal maxPrice, int offset, int limit, ProductSort sort);
    List<Product> getSpecialOffer();
    CustomPage<ProductResponse> getAllProducts(int offset, int limit, ProductSort sort);
    ProductResponse getProductById(int productId);
    Product saveProduct(ProductRequest productRequest) throws IOException;
    Product saveProduct(Product product);
    boolean deleteProduct(int productId);
    Product updatePrice(int productId, int newPrice);
    Product updateDiscount(int productId, int newDiscount);
    Product updateCount(int productId, int newCount);
    Product updateCustomerArticle(int id, String customerArticle);
    void toggleSpecialOffer(int id);
}
