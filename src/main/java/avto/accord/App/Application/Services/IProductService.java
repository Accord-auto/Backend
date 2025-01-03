package avto.accord.App.Application.Services;

import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Models.Product.ProductSort;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface IProductService {
    Product getSpecialOffer();
    Page<Product> getAllProducts(int offset, int limit, ProductSort sort);
    Product getProductById(int productId);
    Product saveProduct(ProductRequest productRequest) throws IOException;
    Product saveProduct(Product product);
    boolean deleteProduct(int productId);
    Product updatePrice(int productId, int newPrice);
    Product updateDiscount(int productId, int newDiscount);
    Product updateCount(int productId, int newCount);
    Product updateCustomerArticle(int id, String customerArticle);
}
