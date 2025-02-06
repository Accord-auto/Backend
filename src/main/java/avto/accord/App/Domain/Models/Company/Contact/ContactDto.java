package avto.accord.App.Domain.Models.Company.Contact;

import lombok.Value;

/**
 * DTO for {@link avto.accord.App.Domain.Models.Company.Contact.Contact}
 */
@Value
public class ContactDto {
    String phoneNumber;
    String email;
}