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

    public static Specification<Product> inCategories(List<Integer> categoryIds) {
        return (root, query, cb) ->
                categoryIds == null || categoryIds.isEmpty()
                        ? cb.conjunction()
                        : root.get("category").get("id").in(categoryIds);
    }

    public static Specification<Product> hasBrands(List<String> brands) {
        return (root, query, cb) ->
                brands == null || brands.isEmpty()
                        ? cb.conjunction()
                        : root.get("brand").in(brands);
    }

    public static Specification<Product> hasPriceBetween(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return cb.conjunction();

            Join<Product, Price> priceJoin = root.join("price");
            List<Predicate> predicates = new ArrayList<>();

            if (min != null) predicates.add(cb.ge(priceJoin.get("value"), min));
            if (max != null) predicates.add(cb.le(priceJoin.get("value"), max));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> hasProperties(Map<String, List<String>> properties) {
        return (root, query, cb) -> {
            if (properties == null || properties.isEmpty()) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : properties.entrySet()) {
                String propertyName = entry.getKey();
                List<String> propertyValues = entry.getValue();

                Subquery<ProductProperty> sq = query.subquery(ProductProperty.class);
                Root<ProductProperty> pp = sq.from(ProductProperty.class);
                Join<ProductProperty, Property> prop = pp.join("property");

                sq.select(pp.get("product").get("id"))
                        .where(
                                cb.and(
                                        cb.equal(prop.get("name"), propertyName),
                                        pp.get("value").in(propertyValues),
                                        cb.equal(pp.get("product").get("id"), root.get("id"))
                                )
                        );
                predicates.add(cb.exists(sq));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}