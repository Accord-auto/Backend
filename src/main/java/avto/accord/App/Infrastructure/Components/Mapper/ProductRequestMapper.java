package avto.accord.App.Infrastructure.Components.Mapper;

import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Models.Product.ProductRequestPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductRequestMapper {
    ProductRequestMapper INSTANCE = Mappers.getMapper(ProductRequestMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "count", target = "count")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "countType", target = "countType")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "article", target = "article")
    @Mapping(source = "categoryId", target = "categoryId")
    @Mapping(source = "properties", target = "properties")
    ProductRequest toProductRequest(ProductRequestPayload productRequestPayload);
}