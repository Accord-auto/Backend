package avto.accord.App.Web.Controllers.PropertyController;

import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Models.Property.PropertyDTO;
import avto.accord.App.Domain.Models.Property.PropertyRequest;
import avto.accord.App.Domain.Models.Property.PropertySimpleDTO;
import avto.accord.App.Domain.Services.PropertyService.PropertyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "характеристики")
@RequiredArgsConstructor
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService _propertyService;
    @GetMapping
    public List<PropertySimpleDTO> getAllProperties() {
        return _propertyService.getAllProperties();
    }
    @GetMapping("/{id}")
    public PropertyDTO getPropertyById(@PathVariable int id) {
        return _propertyService.getPropertyById(id);
    }

    @PostMapping
    public Property addProperty(@RequestBody PropertyRequest request) {
        return _propertyService.saveProperty(request);
    }
    @DeleteMapping("/{id}")
    public void deleteProperty(@PathVariable int id) {
        _propertyService.deleteProperty(id);
    }
}
