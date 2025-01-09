package avto.accord.App.Domain.Models.Price;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response object for Price")
public class PriceResponse {
    @Schema(description = "Price value", example = "100")
    private int value;

    @Schema(description = "Price discount", example = "10")
    private int discount;
}
