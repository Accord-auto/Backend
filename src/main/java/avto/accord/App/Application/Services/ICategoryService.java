package avto.accord.App.Application.Services;

import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Category.CategoryRequest;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category saveCategory(CategoryRequest category);
    void deleteCategory(int id);
}
