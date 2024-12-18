package avto.accord.App.Domain.Services.ProductRequestService;

import avto.accord.App.Application.Services.IProductService;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Models.Product.ProductRequestPayload;
import avto.accord.App.Infrastructure.Components.Mapper.ProductRequestMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductRequestService {
    @Autowired
    private IProductService productService;

    public Product createProduct(MultipartFile mainPhoto, List<MultipartFile> additionalPhotos, String productRequestPayloadJson) throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductRequestPayload productRequestPayload = objectMapper.readValue(productRequestPayloadJson, ProductRequestPayload.class);

        ProductRequest productRequest = ProductRequestMapper.INSTANCE.toProductRequest(productRequestPayload);
        productRequest.setMainPhoto(mainPhoto);
        productRequest.setAdditionalPhotos(additionalPhotos);

        return productService.saveProduct(productRequest);
    }
}