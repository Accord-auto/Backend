package avto.accord.App.Application.Services;

import avto.accord.App.Domain.Models.ProductProperty.DeletePropertyValueRequest;
import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Models.Property.PropertyDTO;
import avto.accord.App.Domain.Models.Property.PropertyRequest;
import avto.accord.App.Domain.Models.Property.PropertySimpleDTO;

import java.util.List;

public interface IPropertyService {
    ProductProperty findProductPropertyByPropertyIdAndValue(int propertyId, String value);
    Property saveProperty(PropertyRequest property);
    List<PropertySimpleDTO> getAllProperties();
    PropertyDTO getPropertyById(int id);
    Property getPropertyByIdOnly(int id);
    void deleteProperty(int id);
    PropertyDTO addPropertyValue(ProductPropertyRequest request);
    PropertyDTO deletePropertyValue(DeletePropertyValueRequest request);
}
