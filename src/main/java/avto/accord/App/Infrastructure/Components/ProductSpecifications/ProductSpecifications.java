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
        return (root, query, cb) -> {
            try {
                if (categoryId == null) {
                    log.debug("Category ID is null, skipping category filter.");
                    return cb.conjunction(); // Возвращаем "истину" вместо null
                }
                log.debug("Applying category filter with ID: {}", categoryId);
                return cb.equal(root.get("category").get("id"), categoryId);
            } catch (Exception e) {
                log.error("Error occurred while applying category filter: {}", e.getMessage(), e);
                return cb.conjunction(); // Возвращаем "истину" в случае ошибки
            }
        };
    }
    /**
     * Specification для фильтрации товаров по цене.
     *
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @return Specification<Product>
     */
    public static Specification<Product> priceBetween(Integer minPrice, Integer maxPrice) {
        return (root, query, cb) -> {
            try {
                if (minPrice == null && maxPrice == null) {
                    log.debug("Both minPrice and maxPrice are null, skipping price filter.");
                    return null;
                }
                Join<Product, Price> priceJoin = root.join("price");

                if (minPrice != null && maxPrice != null) {
                    log.debug("Applying price range filter between {} and {}", minPrice, maxPrice);
                    return cb.between(priceJoin.get("value"), minPrice, maxPrice);
                } else if (minPrice != null) {
                    log.debug("Applying price filter greater than or equal to {}", minPrice);
                    return cb.greaterThanOrEqualTo(priceJoin.get("value"), minPrice);
                } else {
                    log.debug("Applying price filter less than or equal to {}", maxPrice);
                    return cb.lessThanOrEqualTo(priceJoin.get("value"), maxPrice);
                }
            } catch (Exception e) {
                log.error("Error occurred while applying price filter: {}", e.getMessage(), e);
                return null;
            }
        };
    }


    /**
     * Specification для фильтрации товаров по свойствам.
     *
     * @param properties карта свойств и их значений
     * @return Specification<Product>
     */
    public static Specification<Product> hasProperties(Map<String, List<String>> properties) {
        return (root, query, cb) -> {
            try {
                if (properties == null || properties.isEmpty()) {
                    log.debug("Properties map is empty, skipping property filter.");
                    return cb.conjunction();
                }

                log.debug("Applying property filters: {}", properties);

                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String, List<String>> entry : properties.entrySet()) {
                    String propertyName = entry.getKey();
                    List<String> propertyValues = entry.getValue();

                    if (propertyValues == null || propertyValues.isEmpty()) {
                        continue;
                    }

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

                    predicates.add(cb.or(valuePredicates.toArray(new Predicate[0])));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            } catch (Exception e) {
                log.error("Error occurred while applying property filters: {}", e.getMessage(), e);
                return cb.conjunction();
            }
        };
    }

    /**
     * Создает общую спецификацию на основе переданного фильтра.
     *
     * @param filter объект фильтра
     * @return Specification<Product>
     */
    public static Specification<Product> buildSpecification(ProductFilter filter) {
        try {
            log.debug("Building product specification from filter: {}", filter);
            return Specification.where(hasCategory(filter.getCategoryId()))
                    .and(priceBetween(filter.getMinPrice(), filter.getMaxPrice()))
                    .and(hasProperties(filter.getProperties()));
        } catch (Exception e) {
            log.error("Error occurred while building product specification: {}", e.getMessage(), e);
            return Specification.where(null);
        }
    }
}