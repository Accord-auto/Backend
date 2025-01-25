package avto.accord.App.Domain.Models.ProductProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductProperties {
    private Map<String, List<PropertyValue>> properties;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PropertyValue {
        private String value;
    }
}

