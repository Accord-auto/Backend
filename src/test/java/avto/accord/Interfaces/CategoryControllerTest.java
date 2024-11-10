package avto.accord.Interfaces;

import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Category.CategoryRequest;
import avto.accord.App.Domain.Services.CategoryService.CategoryService;
import avto.accord.App.Web.Controllers.CategoryController.CategoryController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCategories() throws Exception {
        List<Category> categories = Arrays.asList(
                new Category(1, "Category 1", null),
                new Category(2, "Category 2", null)
        );

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/categories/"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(categories)));
    }

    @Test
    public void testAddCategory() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("New Category");
        Category category = new Category(1, "New Category", null);

        when(categoryService.saveCategory(any(CategoryRequest.class))).thenReturn(category);

        mockMvc.perform(post("/categories/")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(category)));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        int categoryId = 1;

        doNothing().when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(delete("/categories/{id}", categoryId))
                .andExpect(status().isOk());
    }
}
