package avto.accord.App.Domain.Models.Product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum ProductSort {
    ID_ASC(Sort.by(Sort.Direction.ASC, "id")),
    ID_DESC(Sort.by(Sort.Direction.DESC, "id")),
    NAME_ASC(Sort.by(Sort.Direction.ASC, "name")),
    NAME_DESC(Sort.by(Sort.Direction.DESC, "name")),
    BRAND_ASC(Sort.by(Sort.Direction.ASC, "brand")),
    BRAND_DESC(Sort.by(Sort.Direction.DESC, "brand")),
    COUNT_ASC(Sort.by(Sort.Direction.ASC, "count")),
    COUNT_DESC(Sort.by(Sort.Direction.DESC, "count")),
    PRICE_ASC(Sort.by(Sort.Direction.ASC, "price.value")),
    PRICE_DESC(Sort.by(Sort.Direction.DESC, "price.value")),
    COUNT_TYPE_ASC(Sort.by(Sort.Direction.ASC, "countType")),
    COUNT_TYPE_DESC(Sort.by(Sort.Direction.DESC, "countType")),
    DESCRIPTION_ASC(Sort.by(Sort.Direction.ASC, "description")),
    DESCRIPTION_DESC(Sort.by(Sort.Direction.DESC, "description")),
    ARTICLE_ASC(Sort.by(Sort.Direction.ASC, "article")),
    ARTICLE_DESC(Sort.by(Sort.Direction.DESC, "article")),
    SPECIAL_OFFER_ASC(Sort.by(Sort.Direction.ASC, "specialOffer")),
    SPECIAL_OFFER_DESC(Sort.by(Sort.Direction.DESC, "specialOffer")),
    CUSTOMER_ARTICLE_ASC(Sort.by(Sort.Direction.ASC, "customerArticle")),
    CUSTOMER_ARTICLE_DESC(Sort.by(Sort.Direction.DESC, "customerArticle"));

    private final Sort sortValue;

}
