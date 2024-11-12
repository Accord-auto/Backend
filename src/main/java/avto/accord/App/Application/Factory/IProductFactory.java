package avto.accord.App.Application.Factory;

import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;

import java.io.IOException;

public interface IProductFactory {
    Product createProduct(ProductRequest productRequest) throws IOException;
}