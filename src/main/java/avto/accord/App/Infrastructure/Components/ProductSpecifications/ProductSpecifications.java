package avto.accord.App.Infrastructure.Components.ProductSpecifications;

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
    /*
    * Метод для создания спецификации поиска товаров по категории.
    */
    public static Specification<Product> hasCategory(Integer categoryId) {
        return (root, query, criteriaBuilder) ->
                categoryId != null
                        ? criteriaBuilder.equal(root.get("category").get("id"), categoryId)
                        : criteriaBuilder.conjunction();
    }

    /*
     * Метод для создания спецификации поиска товаров по заданным свойствам.
    */
    public static Specification<Product> hasProperties(Map<String, List<String>> properties) {
        return (root, query, cb) -> {
            if (properties == null || properties.isEmpty()) {
                return cb.conjunction(); // Если фильтров нет, возвращаем пустое условие
            }

            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : properties.entrySet()) {
                String propName = entry.getKey();
                List<String> propValues = entry.getValue();

                Subquery<Integer> subquery = query.subquery(Integer.class);
                Root<ProductProperty> pp = subquery.from(ProductProperty.class);
                Join<ProductProperty, Property> propJoin = pp.join("property");
                subquery.select(pp.get("product").get("id"))
                        .where(
                                cb.and(
                                        cb.equal(propJoin.get("name"), propName), // Фильтр по имени свойства
                                        pp.get("value").in(propValues)           // Фильтр по значениям свойства
                                )
                        );
                predicates.add(cb.in(root.get("id")).value(subquery));
            }
            query.distinct(true); // Убираем дубликаты
            return cb.and(predicates.toArray(new Predicate[0])); // Объединяем все условия через AND
        };
    }

    /*
     * Метод для создания спецификации поиска товаров по диапазону цен.
    */
    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, criteriaBuilder) -> {
            if (min == null && max == null) return criteriaBuilder.conjunction();
            Path<BigDecimal> pricePath = root.get("price").get("value");

            if (min != null && max != null) {
                return criteriaBuilder.between(pricePath, min, max);
            } else if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(pricePath, min);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(pricePath, max);
            }
        };
    }
}