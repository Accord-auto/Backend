package avto.accord.App.Domain.Models.Property;

import avto.accord.App.Domain.Models.ProductProperty.PropertyValueDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDTO {
    private int id;
    private String name;
    private List<PropertyValueDTO> values;
}
