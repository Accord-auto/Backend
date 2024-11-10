package avto.accord.App.Domain.Repositories.Category;


import avto.accord.App.Domain.Models.Category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}

