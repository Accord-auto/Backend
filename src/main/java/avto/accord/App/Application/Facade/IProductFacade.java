package avto.accord.App.Application.Facade;

import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;

import java.io.IOException;

public interface IProductFacade {
    Product createProduct(ProductRequest productRequest) throws IOException;
}