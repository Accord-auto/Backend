package avto.accord.App.Domain.Models.ProductProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyRequest {
    private int propertyId;
    private String value;
}
