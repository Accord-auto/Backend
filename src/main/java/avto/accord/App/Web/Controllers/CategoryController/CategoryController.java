package avto.accord.App.Web.Controllers.CategoryController;

import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Category.CategoryRequest;
import avto.accord.App.Domain.Services.CategoryService.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(_categoryService.getAllCategories());
    }
    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody CategoryRequest request) {
        Category category = _categoryService.saveCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        log.info("запрос на удаление категории по id: ", id);
        _categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
