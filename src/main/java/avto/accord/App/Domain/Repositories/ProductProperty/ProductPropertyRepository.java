package avto.accord.App.Domain.Repositories.ProductProperty;
import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Integer> {
}