package avto.accord.App.Domain.Repositories.Product;


import avto.accord.App.Domain.Models.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAllBy(Pageable pageable);
    @Query(value = "SELECT * FROM product p WHERE p.special_offer = true", nativeQuery = true)
    List<Product> findRandomSpecialOfferProduct();
}
