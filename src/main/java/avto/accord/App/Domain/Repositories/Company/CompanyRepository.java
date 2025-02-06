package avto.accord.App.Domain.Repositories.Company;

import avto.accord.App.Domain.Models.Company.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findById(int id);
}
