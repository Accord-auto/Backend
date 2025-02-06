package avto.accord.App.Infrastructure.Components.ProductSpecifications;

import avto.accord.App.Domain.Models.FilterRequest.ProductFilter;
import avto.accord.App.Domain.Models.Price.Price;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import avto.accord.App.Domain.Models.Property.Property;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/*
 * Класс для создания спецификаций поиска товаров.
 */

@Slf4j
@Component
public class ProductSpecifications {

    public static Specification<Product> hasCategory(Integer categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> priceBetween(Integer minPrice, Integer maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) return null;
            Join<Product, Price> priceJoin = root.join("price");

            if (minPrice != null && maxPrice != null) {
                return cb.between(priceJoin.get("value"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(priceJoin.get("value"), minPrice);
            } else {
                return cb.lessThanOrEqualTo(priceJoin.get("value"), maxPrice);
            }
        };
    }

    public static Specification<Product> hasProperties(Map<String, List<String>> properties) {
        return (root, query, cb) -> {
            if (properties == null || properties.isEmpty()) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : properties.entrySet()) {
                String propertyName = entry.getKey();
                List<String> propertyValues = entry.getValue();
                List<Predicate> valuePredicates = new ArrayList<>();
                for (String value : propertyValues) {
                    Subquery<ProductProperty> subquery = query.subquery(ProductProperty.class);
                    Root<ProductProperty> ppRoot = subquery.from(ProductProperty.class);
                    subquery.select(ppRoot)
                            .where(
                                    cb.equal(ppRoot.get("product"), root),
                                    cb.equal(ppRoot.get("property").get("name"), propertyName),
                                    cb.equal(ppRoot.get("value"), value)
                            );
                    valuePredicates.add(cb.exists(subquery));
                }
                predicates.add(cb.and(valuePredicates.toArray(new Predicate[0])));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    public static Specification<Product> buildSpecification(ProductFilter filter) {
        return Specification.where(hasCategory(filter.getCategoryId()))
                .and(priceBetween(filter.getMinPrice(), filter.getMaxPrice()))
                .and(hasProperties(filter.getProperties()));
    }
}