package avto.accord.App.Domain.Repositories.Company;

import avto.accord.App.Domain.Models.Company.Company;
import avto.accord.App.Domain.Models.Company.TypeCompany.TypeCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findById(int id);
    List<Company> findAllByTypeCompany(TypeCompany typeCompany);

    Optional<Company> findByTypeCompany(TypeCompany typeCompany);
}
