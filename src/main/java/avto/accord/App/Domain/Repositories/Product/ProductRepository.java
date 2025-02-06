package avto.accord.App.Domain.Repositories.Product;


import avto.accord.App.Domain.Models.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    @Query(value = "SELECT * FROM product p WHERE p.special_offer = true", nativeQuery = true)
    List<Product> findRandomSpecialOfferProduct();

    Optional<Product> findByCustomerArticle(String customerArticle);

    Optional<Product> findByArticle(String article);

    default void toggleSpecialOffer(int id) {
        Product product = findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setSpecialOffer(!product.isSpecialOffer());
        save(product);
    }
}
