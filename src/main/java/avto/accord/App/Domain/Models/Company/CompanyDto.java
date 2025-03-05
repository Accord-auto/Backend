package avto.accord.App.Domain.Models.Company;

import avto.accord.App.Domain.Models.Company.Adress.AddressDto;
import avto.accord.App.Domain.Models.Company.Contact.ContactDto;
import avto.accord.App.Domain.Models.Company.TypeCompany.TypeCompany;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

/**
 * DTO for {@link Company}
 */
@Value
public class CompanyDto {
    String name;
    AddressDto address;
    List<ContactDto> contacts;
    TypeCompany typeCompany;
}