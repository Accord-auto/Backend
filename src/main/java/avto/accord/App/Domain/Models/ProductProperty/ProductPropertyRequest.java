package avto.accord.App.Domain.Models.ProductProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPropertyRequest {
    private int propertyId;
    private String value;
}
