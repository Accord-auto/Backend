package avto.accord.App.Application.Services;

import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Models.Property.PropertyRequest;

public interface IPropertyService {
    Property saveProperty(PropertyRequest property);
    void deleteProperty(int id);
}
