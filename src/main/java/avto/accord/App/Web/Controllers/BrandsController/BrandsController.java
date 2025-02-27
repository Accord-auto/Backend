package avto.accord.App.Web.Controllers.BrandsController;

import avto.accord.App.Application.Services.IProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "бренды")
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandsController {
    private final IProductService productService;
    @GetMapping
    public List<String> getAllBrands() {
        try {
            return productService.getBrands();
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
