package avto.accord.App.Domain.Models.GeoNamesResponse;

import lombok.Data;

import java.util.List;

@Data
public class GeoNamesResponse {
    private List<GeoName> geonames;

    @Data
    public static class GeoName {
        private String name;
        private String countryName;
        private String adminName1; // Region/State name
        private String adminCode1; // Region/State code
     }
}
