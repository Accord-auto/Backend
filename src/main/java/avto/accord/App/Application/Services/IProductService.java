package avto.accord.App.Application.Services;

import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface IProductService {
    Page<Product> getAllProducts(int offset, int limit);
    Product getProductById(int productId);
    Product saveProduct(ProductRequest productRequest) throws IOException;
    boolean deleteProduct(int productId);
    Product updatePrice(int productId, int newPrice);
    Product updateDiscount(int productId, int newDiscount);
    Product updateCount(int productId, int newCount);
}
