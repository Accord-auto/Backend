package avto.accord.App.Domain.Repositories.Product;


import avto.accord.App.Domain.Models.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAllBy(Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.specialOffer = true ORDER BY FUNCTION('RAND')")
    Product findRandomSpecialOfferProduct();
}
