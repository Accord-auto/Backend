package avto.accord.App.Domain.Repositories.Product;


import avto.accord.App.Domain.Models.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAllBy(Pageable pageable);
    @Query(value = "SELECT * FROM product p WHERE p.special_offer = true ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Product findRandomSpecialOfferProduct();
}
