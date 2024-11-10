package avto.accord.App.Web.Controllers.ProductController;

import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Services.ProductService.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@Tag(name = "товары")
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public Page<Product> getAllProducts(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit
    ) {
        return productService.getAllProducts(offset, limit);
    }
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public Product createProduct(@ModelAttribute ProductRequest productRequest) throws IOException {
        return productService.saveProduct(productRequest);
    }
    @PutMapping("/{id}/price")
    public Product updatePrice(@PathVariable int id, @RequestParam int newPrice) {
        return productService.updatePrice(id, newPrice);
    }

    @PutMapping("/{id}/discount")
    public Product updateDiscount(@PathVariable int id, @RequestParam int newDiscount) {
        return productService.updateDiscount(id, newDiscount);
    }

    @PutMapping("/{id}/count")
    public Product updateCount(@PathVariable int id, @RequestParam int newCount) {
        return productService.updateCount(id, newCount);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }
}
