package avto.accord.App.Domain.Models.Partners;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartnershipRequest {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String country;
    private String region;
    private String city;
    private String activitySphere;
    private String linkToCompanyWebsite;
    private String message;
}
