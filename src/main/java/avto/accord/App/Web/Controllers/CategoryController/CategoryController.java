package avto.accord.App.Web.Controllers.CategoryController;

import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Category.CategoryRequest;
import avto.accord.App.Domain.Services.CategoryService.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "категории")
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService _categoryService;
    @GetMapping("/")
    public List<Category> getAllCategories() {
        return _categoryService.getAllCategories();
    }
    @PostMapping("/")
    public Category addCategory(@RequestBody CategoryRequest request) {
        return _categoryService.saveCategory(request);
    }
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable int id) {
        _categoryService.deleteCategory(id);
    }
}
