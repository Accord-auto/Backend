package avto.accord.App.Domain.Services.ProductRequestService;

import avto.accord.App.Application.Services.IProductService;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Models.Product.ProductRequestPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ProductRequestService {
    @Autowired
    private IProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    public Product createProduct(MultipartFile mainPhoto, List<MultipartFile> additionalPhotos, String productRequestPayloadJson) throws IOException {
        try {
            ProductRequestPayload productRequestPayload = objectMapper.readValue(productRequestPayloadJson, ProductRequestPayload.class);

            // Validate photos
            if (mainPhoto == null || additionalPhotos == null || additionalPhotos.isEmpty()) {
                throw new IllegalArgumentException("Main photo or additional photos are missing.");
            }

            if (additionalPhotos.size() > 3) {
                throw new IllegalArgumentException("Too many additional photos. Maximum allowed is 3.");
            }

            ProductRequest productRequest = mapToProductRequest(productRequestPayload, mainPhoto, additionalPhotos);

            return productService.saveProduct(productRequest);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse productRequestPayloadJson", e);
            throw e;
        } catch (Exception e) {
            log.error("Error creating product", e);
            throw e;
        }
    }

    private ProductRequest mapToProductRequest(ProductRequestPayload payload, MultipartFile mainPhoto, List<MultipartFile> additionalPhotos) {
        if (payload == null) {
            throw new IllegalArgumentException("ProductRequestPayload is null");
        }
        if (mainPhoto == null) {
            throw new IllegalArgumentException("Main photo is null");
        }
        if (additionalPhotos == null) {
            throw new IllegalArgumentException("Additional photos are null");
        }
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(payload.getName());
        productRequest.setBrand(payload.getBrand());
        productRequest.setCount(payload.getCount());
        productRequest.setPrice(payload.getPrice());
        productRequest.setCountType(payload.getCountType());
        productRequest.setDescription(payload.getDescription());
        productRequest.setArticle(payload.getArticle());
        productRequest.setMainPhoto(mainPhoto);
        productRequest.setAdditionalPhotos(additionalPhotos);
        productRequest.setCategoryId(payload.getCategoryId());
        productRequest.setProperties(payload.getProperties());
        productRequest.setSpecialOffer(payload.isSpecialOffer());
        productRequest.setCustomerArticle(payload.getCustomerArticle());
        return productRequest;
    }
}