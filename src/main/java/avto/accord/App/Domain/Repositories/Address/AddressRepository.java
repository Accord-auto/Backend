package avto.accord.App.Domain.Repositories.Address;

import avto.accord.App.Domain.Models.Company.Adress.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}