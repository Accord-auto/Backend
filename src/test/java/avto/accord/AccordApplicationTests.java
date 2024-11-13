package avto.accord;

import avto.accord.App.Web.Controllers.ArticleController.ArticleControllerTest;
import avto.accord.App.Web.Controllers.CategoryController.CategoryControllerTest;
import avto.accord.App.Web.Controllers.ProductController.ProductControllerTest;
import avto.accord.App.Web.Controllers.PropertyController.PropertyControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccordApplicationTests {
    @Test
    void contextLoads() {
    }
    @Test
    void testProductController() throws Exception {
        ProductControllerTest productControllerTest = new ProductControllerTest();
        productControllerTest.setUp();
        productControllerTest.testGetAllProducts();
        productControllerTest.testGetProductById();
        productControllerTest.testCreateProduct();
        productControllerTest.testUpdateProductPrice();
        productControllerTest.testUpdateProductDiscount();
        productControllerTest.testUpdateProductCount();
        productControllerTest.testDeleteProduct();
    }

    @Test
    void testCategoryController() throws Exception {
        CategoryControllerTest categoryControllerTest = new CategoryControllerTest();
        categoryControllerTest.setUp();
        categoryControllerTest.testAddCategory();
        categoryControllerTest.testDeleteCategory();
        categoryControllerTest.testGetAllCategories();
    }

    @Test
    void testPropertyController() throws Exception {
        PropertyControllerTest propertyControllerTest = new PropertyControllerTest();
        propertyControllerTest.setUp();
        propertyControllerTest.testAddProperty();
        propertyControllerTest.testDeleteProperty();
    }

    @Test
    void testArticleController() throws Exception {
        ArticleControllerTest articleControllerTest = new ArticleControllerTest();
        articleControllerTest.setUp();
        articleControllerTest.testDeleteArticle();
        articleControllerTest.testGetAllArticles();
        articleControllerTest.testGetArticleById();
        articleControllerTest.testCreateArticle();
        articleControllerTest.testUpdateArticle();
    }
}
