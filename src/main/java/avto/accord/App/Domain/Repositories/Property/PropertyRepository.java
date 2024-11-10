package avto.accord.App.Domain.Repositories.Property;
import avto.accord.App.Domain.Models.Property.Property;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PropertyRepository extends JpaRepository<Property, Integer> {
}

