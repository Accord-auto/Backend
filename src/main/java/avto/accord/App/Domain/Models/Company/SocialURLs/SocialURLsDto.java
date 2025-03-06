package avto.accord.App.Domain.Models.Company.SocialURLs;

import lombok.Value;

/**
 * DTO for {@link SocialURLs}
 */
@Value
public class SocialURLsDto {
    String type;
    String url;
}