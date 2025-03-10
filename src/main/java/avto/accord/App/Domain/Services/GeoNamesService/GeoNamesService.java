package avto.accord.App.Domain.Services.GeoNamesService;

import avto.accord.App.Domain.Models.GeoNamesResponse.GeoNamesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeoNamesService {
    @Value("${geonames.api.url}")
    private String GEONAMES_API_URL;

    @Value("${spring.datasource.username}")
    private String USERNAME;

    private final RestTemplate restTemplate;

    public GeoNamesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeoNamesResponse searchCities(int startRow, int maxRows) {
        // Коды стран СНГ
        String cngCountries = "RU,BY,UA,KZ,UZ,AZ,AM,KG,TJ,TM,MD";
        String url = UriComponentsBuilder.fromHttpUrl(GEONAMES_API_URL)
                .queryParam("startRow", startRow)
                .queryParam("maxRows", maxRows)
                .queryParam("username", USERNAME)
                .queryParam("featureClass", "P") // Только города
                .queryParam("country", cngCountries) // Ограничение по странам СНГ
                .toUriString();

        return restTemplate.getForObject(url, GeoNamesResponse.class);
    }
}