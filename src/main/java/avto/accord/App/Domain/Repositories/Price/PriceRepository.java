package avto.accord.App.Domain.Repositories.Price;


import avto.accord.App.Domain.Models.Price.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PriceRepository extends JpaRepository<Price, Integer> {
    Optional<Price> findTopByOrderByValueDesc();
}
