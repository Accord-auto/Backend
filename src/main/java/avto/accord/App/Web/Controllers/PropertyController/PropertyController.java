package avto.accord.App.Web.Controllers.PropertyController;

import avto.accord.App.Domain.Models.ProductProperty.DeletePropertyValueRequest;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
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
    private PropertyService propertyService;
    @GetMapping
    public List<PropertySimpleDTO> getAllProperties() {
        return propertyService.getAllProperties();
    }
    @PostMapping("/add-value")
    public PropertyDTO addPropertyValue(@RequestBody ProductPropertyRequest request) {
        return propertyService.addPropertyValue(request);
    }
    @DeleteMapping("/delete-value")
    public PropertyDTO deletePropertyValue(@RequestBody DeletePropertyValueRequest request) {
        return propertyService.deletePropertyValue(request);
    }
    @GetMapping("/{id}")
    public PropertyDTO getPropertyById(@PathVariable int id) {
        return propertyService.getPropertyById(id);
    }

    @PostMapping
    public Property addProperty(@RequestBody PropertyRequest request) {
        return propertyService.saveProperty(request);
    }
    @DeleteMapping("/{id}")
    public void deleteProperty(@PathVariable int id) {
        propertyService.deleteProperty(id);
    }
}
