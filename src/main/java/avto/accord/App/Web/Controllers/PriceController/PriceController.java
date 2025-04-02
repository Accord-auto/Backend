package avto.accord.App.Web.Controllers.PriceController;

import avto.accord.App.Domain.Models.Price.Price;
import avto.accord.App.Domain.Services.PriceService.PriceService;
import avto.accord.App.Infrastructure.Annotations.PublicEndpoint.PublicEndpoint;
import avto.accord.App.Infrastructure.Exception.ProductNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Цены")
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;
    @GetMapping("/max-price" )
    @PublicEndpoint
    public Price getMaxPrice() {
        return priceService.getMaxPrice().orElseThrow(() -> new ExpressionException("max price not found!"));
    }
}
