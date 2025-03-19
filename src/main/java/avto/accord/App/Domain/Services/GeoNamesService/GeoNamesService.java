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

    public GeoNamesResponse getRussianRegions(int startRow, int maxRows) {
        String url = UriComponentsBuilder.fromHttpUrl(GEONAMES_API_URL)
                .queryParam("startRow", startRow)
                .queryParam("maxRows", maxRows)
                .queryParam("username", USERNAME)
                .queryParam("country", "RU")
                .queryParam("featureCode", "ADM1") // Административные регионы первого уровня
                .queryParam("style", "FULL")
                .queryParam("lang", "ru")
                .toUriString();

        return restTemplate.getForObject(url, GeoNamesResponse.class);
    }

    public GeoNamesResponse getCitiesByRegion(String regionId, int startRow, int maxRows) {
        String url = UriComponentsBuilder.fromHttpUrl(GEONAMES_API_URL)
                .queryParam("startRow", startRow)
                .queryParam("maxRows", maxRows)
                .queryParam("username", USERNAME)
                .queryParam("country", "RU")
                .queryParam("featureClass", "P")
                .queryParam("style", "FULL")
                .queryParam("cities", "cities15000")
                .queryParam("adminCode1", regionId) // Фильтрация по коду региона
                .queryParam("lang", "ru")
                .toUriString();

        return restTemplate.getForObject(url, GeoNamesResponse.class);
    }
}