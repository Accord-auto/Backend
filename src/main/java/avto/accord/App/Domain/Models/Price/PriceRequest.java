package avto.accord.App.Domain.Models.Price;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceRequest {
    private int discount;
    private int value;
}
