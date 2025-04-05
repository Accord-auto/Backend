package avto.accord.App.Web.Controllers.CategoryController;

import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Category.CategoryRequest;
import avto.accord.App.Domain.Services.CategoryService.CategoryService;
import avto.accord.App.Infrastructure.Annotations.PublicEndpoint.PublicEndpoint;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    @PublicEndpoint
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(_categoryService.getAllCategories());
    }
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Category> addCategory(@RequestBody CategoryRequest request) {
        try{
            Category category = _categoryService.saveCategory(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        }catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        log.info("запрос на удаление категории по id: ", id);
        _categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
