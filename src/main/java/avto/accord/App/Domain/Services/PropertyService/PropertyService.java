package avto.accord.App.Domain.Services.PropertyService;

import avto.accord.App.Application.Services.IPropertyService;
import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Models.Property.PropertyDTO;
import avto.accord.App.Domain.Models.Property.PropertyRequest;
import avto.accord.App.Domain.Models.Property.PropertySimpleDTO;
import avto.accord.App.Domain.Repositories.Property.PropertyRepository;
import avto.accord.App.Infrastructure.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService implements IPropertyService {
    private final PropertyRepository _propertyRepository;

    @Override
    public Property saveProperty(PropertyRequest property) {
        try {
            Property newProperty = new Property();
            newProperty.setName(property.getName());
            return _propertyRepository.save(newProperty);
        } catch (Exception e) {
            throw e;
        }
    }
    public List<PropertySimpleDTO> getAllProperties() {
        List<Property> properties = _propertyRepository.findAll();
        return properties.stream()
                .map(property -> new PropertySimpleDTO(property.getId(), property.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public PropertyDTO getPropertyById(int id) {
        Property property = _propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));

        List<String> values = property.getProductProperties().stream()
                .map(ProductProperty::getValue)
                .collect(Collectors.toList());

        return new PropertyDTO(property.getId(), property.getName(), values);
    }
    @Override
    public Property getPropertyByIdOnly(int id) {
        return _propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));
    }


    @Override
    public void deleteProperty(int id) {
        _propertyRepository.deleteById(id);
    }
}
