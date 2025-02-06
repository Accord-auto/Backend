package avto.accord.App.Domain.Models.FilterRequest;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Getter
@Setter
public class ProductFilter {
    private Integer categoryId;
    private Integer minPrice;
    private Integer maxPrice;
    private Map<String, List<String>> properties = new HashMap<>();
}