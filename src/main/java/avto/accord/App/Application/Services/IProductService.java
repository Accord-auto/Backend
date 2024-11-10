package avto.accord.App.Application.Services;

import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface IProductService {
    Page<Product> getAllProducts(int offset, int limit);
    Product getProduct(int id);
    Product saveProduct(ProductRequest product) throws IOException;
}
