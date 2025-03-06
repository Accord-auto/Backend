package avto.accord.App.Domain.Models.Company.Contact;

import avto.accord.App.Domain.Models.Company.SocialURLs.SocialURLsDto;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link avto.accord.App.Domain.Models.Company.Contact.Contact}
 */
@Value
public class ContactDto {
    String phoneNumber;
    String email;
    List<SocialURLsDto> socialURLs = new ArrayList<>();
}