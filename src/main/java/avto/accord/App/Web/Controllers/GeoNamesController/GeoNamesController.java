package avto.accord.App.Web.Controllers.GeoNamesController;

import avto.accord.App.Domain.Models.GeoNamesResponse.GeoNamesResponse;
import avto.accord.App.Domain.Services.GeoNamesService.GeoNamesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "города и регионы")
@RequestMapping("/regions")
public class GeoNamesController {

    private final GeoNamesService geoNamesService;

    public GeoNamesController(GeoNamesService geoNamesService) {
        this.geoNamesService = geoNamesService;
    }

    @GetMapping()
    public GeoNamesResponse getRussianRegions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int maxRows) {
        int startRow = page * maxRows;
        return geoNamesService.getRussianRegions(startRow, maxRows);
    }

    @GetMapping("/{regionId}/cities")
    public GeoNamesResponse getCitiesByRegion(
            @PathVariable String regionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int maxRows) {
        int startRow = page * maxRows;
        return geoNamesService.getCitiesByRegion(regionId, startRow, maxRows);
    }
}