package avto.accord.App.Domain.Services.CategoryService;

import avto.accord.App.Application.Services.ICategoryService;
import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Category.CategoryRequest;
import avto.accord.App.Domain.Repositories.Category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    @Autowired
    private final CategoryRepository _categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return _categoryRepository.findAll();
    }

    /**
     * @param category
     */
    @Override
    public Category saveCategory(CategoryRequest category) {
        Category categoryEntity = new Category();
        categoryEntity.setName(category.getName());
        return _categoryRepository.save(categoryEntity);
    }

    /**
     * @param id
     */
    @Override
    public void deleteCategory(int id) {
        _categoryRepository.deleteById(id);
    }
    public Optional<Category> getCategoryById(int id) {
        return _categoryRepository.findById(id);
    }
}
