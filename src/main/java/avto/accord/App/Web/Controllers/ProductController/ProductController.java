package avto.accord.App.Web.Controllers.ProductController;

import avto.accord.App.Application.Services.IProductService;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequestPayload;
import avto.accord.App.Domain.Models.Product.ProductResponse;
import avto.accord.App.Domain.Models.Product.ProductSort;
import avto.accord.App.Domain.Services.ProductRequestService.ProductRequestService;
import avto.accord.App.Infrastructure.Exception.ErrorResponse;
import avto.accord.App.Infrastructure.Exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "товары")
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ProductRequestService productRequestService;

    @GetMapping
    public Page<ProductResponse> getAllProducts(
            @RequestParam(value = "offset") int offset,
            @RequestParam(value = "limit") int limit,
            @RequestParam(value = "sort", defaultValue = "ID_ASC") ProductSort sort
    ) {
        return productService.getAllProducts(offset, limit, sort);
    }

    @GetMapping("/specialOffer")
    public List<Product> getSpecialOffers() {
        return productService.getSpecialOffer();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable int id) {
        try {
            ProductResponse productResponse = productService.getProductById(id);
            return ResponseEntity.ok(productResponse);
        } catch (ResourceNotFoundException e) {
            log.error("Product not found: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Продукт не найден", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            log.error("Error fetching product", e);
            ErrorResponse errorResponse = new ErrorResponse("Ошибка при получении продукта", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("mainPhoto") MultipartFile mainPhoto,
            @RequestPart("additionalPhotos") List<MultipartFile> additionalPhotos,
            @Parameter(description = "Product request payload", required = true, schema = @Schema(implementation = ProductRequestPayload.class))
            @RequestPart("productRequestPayload") String productRequestPayloadJson
    ) {
        try {
            log.info(productRequestPayloadJson);
            Product createdProduct = productRequestService.createProduct(mainPhoto, additionalPhotos, productRequestPayloadJson);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (IllegalArgumentException e) {
            log.error("Validation error while creating product: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            log.error("Error creating product", e);
            ErrorResponse errorResponse = new ErrorResponse("Ошибка при создании продукта", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PutMapping("/{id}/price")
    public ResponseEntity<Product> updateProductPrice(@PathVariable int id, @RequestBody int price) {
        Product updatedProduct = productService.updatePrice(id, price);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}/discount")
    public ResponseEntity<Product> updateProductDiscount(@PathVariable int id, @RequestBody int discount) {
        Product updatedProduct = productService.updateDiscount(id, discount);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(value = "/{id}/count", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProductCount(@PathVariable int id, @RequestBody int newCount) {
        Product updatedProduct = productService.updateCount(id, newCount);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}/customerArticle")
    public ResponseEntity<Product> updateCustomerArticle(@PathVariable int id, @RequestBody String customerArticle) {
        Product updatedProduct = productService.updateCustomerArticle(id, customerArticle);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(value = "/{id}/toggle-special-offer")
    public ResponseEntity<?> toggleProductSpecialOffer(@PathVariable int id) {
        try {
            productService.toggleSpecialOffer(id);
            return ResponseEntity.ok("Special offer toggled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating special offer");
        }
    }
}