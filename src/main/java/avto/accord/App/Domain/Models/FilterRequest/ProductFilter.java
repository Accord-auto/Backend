package avto.accord.App.Domain.Models.FilterRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Getter
@Setter
public class ProductFilter {
    private List<Integer> categoryIds;
    private List<String> brands;
    private Integer minPrice;
    private Integer maxPrice;
    private Map<String, String> properties = new HashMap<>();
}