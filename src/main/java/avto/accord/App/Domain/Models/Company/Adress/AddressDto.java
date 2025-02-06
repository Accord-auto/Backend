package avto.accord.App.Domain.Models.Company.Adress;

import lombok.Value;

/**
 * DTO for {@link avto.accord.App.Domain.Models.Company.Adress.Address}
 */
@Value
public class AddressDto {
    String street;
    String city;
    String state;
    String zipCode;
}