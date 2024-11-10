package avto.accord.App.Domain.Services.PropertyService;

import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Models.Property.PropertyRequest;
import avto.accord.App.Domain.Repositories.Property.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository _propertyRepository;

    public Property save(PropertyRequest property) {
        try {
            Property newProperty = new Property();
            newProperty.setName(property.getName());
            return _propertyRepository.save(newProperty);
        }catch (Exception e) {
            throw e;
        }
    }
    public Property getPropertyById(int id) {
        try {
            return _propertyRepository.findById(id).orElse(null);
        }catch (Exception e){
            throw e;
        }
    }

    public void delete(int id) {
        _propertyRepository.deleteById(id);
    }
}
