package avto.accord.App.Domain.Services.PropertyService;

import avto.accord.App.Application.Services.IPropertyService;
import avto.accord.App.Domain.Models.ProductProperty.DeletePropertyValueRequest;
import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
import avto.accord.App.Domain.Models.ProductProperty.PropertyValueDTO;
import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Models.Property.PropertyDTO;
import avto.accord.App.Domain.Models.Property.PropertyRequest;
import avto.accord.App.Domain.Models.Property.PropertySimpleDTO;
import avto.accord.App.Domain.Repositories.ProductProperty.ProductPropertyRepository;
import avto.accord.App.Domain.Repositories.Property.PropertyRepository;
import avto.accord.App.Infrastructure.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyService implements IPropertyService {
    private final PropertyRepository propertyRepository;
    private final ProductPropertyRepository productPropertyRepository;

    @Override
    public ProductProperty findProductPropertyByPropertyIdAndValue(int propertyId, String value) {
        return productPropertyRepository.findByPropertyIdAndValue(propertyId, value);
    }

    @Override
    public PropertyDTO addPropertyValue(ProductPropertyRequest request) {
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + request.getPropertyId()));

        // Проверка на уникальность значения свойства
        if (findProductPropertyByPropertyIdAndValue(property.getId(), request.getValue()) != null) {
            throw new IllegalArgumentException("Property value already exists for this property");
        }

        ProductProperty productProperty = new ProductProperty();
        productProperty.setProperty(property);
        productProperty.setValue(request.getValue());

        productPropertyRepository.save(productProperty);

        return getPropertyById(request.getPropertyId());
    }

    @Override
    public Property saveProperty(PropertyRequest property) {
        try {
            Property newProperty = new Property();
            newProperty.setName(property.getName());
            return propertyRepository.save(newProperty);
        } catch (Exception e) {
            log.error("Error saving property: {}", e.getMessage());
            throw new RuntimeException("Failed to save property", e);
        }
    }

    @Override
    public List<PropertySimpleDTO> getAllProperties() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream()
                .map(property -> new PropertySimpleDTO(property.getId(), property.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public PropertyDTO getPropertyById(int id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));

        List<PropertyValueDTO> values = property.getProductProperties().stream()
                .map(productProperty -> new PropertyValueDTO(productProperty.getId(), productProperty.getValue()))
                .collect(Collectors.toList());

        return new PropertyDTO(property.getId(), property.getName(), values);
    }

    @Override
    public Property getPropertyByIdOnly(int id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));
    }

    @Override
    public PropertyDTO deletePropertyValue(DeletePropertyValueRequest request) {
        ProductProperty productProperty = productPropertyRepository.findById(request.getIdValue())
                .orElseThrow(() -> new ResourceNotFoundException("Product property not found with id " + request.getIdValue()));

        productPropertyRepository.delete(productProperty);
        return getPropertyById(request.getIdCharacteristic());
    }

    @Override
    public void deleteProperty(int id) {
        if (!propertyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Property not found with id " + id);
        }
        propertyRepository.deleteById(id);
    }
}

