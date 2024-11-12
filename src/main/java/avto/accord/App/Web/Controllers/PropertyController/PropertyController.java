package avto.accord.App.Web.Controllers.PropertyController;

import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Models.Property.PropertyRequest;
import avto.accord.App.Domain.Services.PropertyService.PropertyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "характеристики")
@RequiredArgsConstructor
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService _propertyService;

    @PostMapping
    public Property addProperty(@RequestBody PropertyRequest request) {
        return _propertyService.saveProperty(request);
    }
    @DeleteMapping("/{id}")
    public void deleteProperty(@PathVariable int id) {
        _propertyService.deleteProperty(id);
    }
}
