package avto.accord.App.Domain.Models.ProductProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeletePropertyValueRequest {
    private int idCharacteristic;
    private int idValue;
}
