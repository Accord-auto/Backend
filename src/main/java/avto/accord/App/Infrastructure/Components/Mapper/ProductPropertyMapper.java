package avto.accord.App.Infrastructure.Components.Mapper;

import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductPropertyMapper {
    ProductPropertyMapper INSTANCE = Mappers.getMapper(ProductPropertyMapper.class);

    @Mapping(source = "propertyRequest.value", target = "value")
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductProperty toProductProperty(ProductPropertyRequest propertyRequest);
}
