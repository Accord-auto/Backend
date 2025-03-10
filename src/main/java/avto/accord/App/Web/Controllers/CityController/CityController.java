package avto.accord.App.Web.Controllers.CityController;

import avto.accord.App.Domain.Models.GeoNamesResponse.GeoNamesResponse;
import avto.accord.App.Domain.Services.GeoNamesService.GeoNamesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final GeoNamesService geoNamesService;

    public CityController(GeoNamesService geoNamesService) {
        this.geoNamesService = geoNamesService;
    }

    @GetMapping
    public GeoNamesResponse getCities(
            @RequestParam(defaultValue = "0") int page) {
        int startRow = page * 10;
        int maxRows = 10;
        return geoNamesService.searchCities(startRow, maxRows);
    }
}