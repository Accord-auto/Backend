package avto.accord.Interfaces;
import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Price.Price;
import avto.accord.App.Domain.Models.Price.PriceRequest;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequest;
import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Services.ProductService.ProductService;
import avto.accord.App.Web.Controllers.ProductController.ProductController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateProduct() throws Exception {
        // Создание запроса
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setBrand("Test Brand");
        productRequest.setCount(10);
        productRequest.setCountType("units");
        productRequest.setDescription("This is a test product.");
        productRequest.setArticle("12345");
        productRequest.setCategoryId(1);

        // Создание мок-файлов для фотографий
        MockMultipartFile mainPhoto = new MockMultipartFile("mainPhoto", "mainPhoto.jpg", MediaType.IMAGE_JPEG_VALUE, "mainPhoto".getBytes());
        MockMultipartFile additionalPhoto1 = new MockMultipartFile("additionalPhotos", "additionalPhoto1.jpg", MediaType.IMAGE_JPEG_VALUE, "additionalPhoto1".getBytes());
        MockMultipartFile additionalPhoto2 = new MockMultipartFile("additionalPhotos", "additionalPhoto2.jpg", MediaType.IMAGE_JPEG_VALUE, "additionalPhoto2".getBytes());

        productRequest.setMainPhoto(mainPhoto);
        productRequest.setAdditionalPhotos(Arrays.asList(additionalPhoto1, additionalPhoto2));

        // Создание мок-объекта для цены
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setDiscount(10);
        priceRequest.setValue(100);
        productRequest.setPrice(priceRequest);

        // Создание мок-объекта для свойств
        ProductPropertyRequest propertyRequest1 = new ProductPropertyRequest();
        propertyRequest1.setPropertyId(1);
        propertyRequest1.setValue("Property Value 1");

        ProductPropertyRequest propertyRequest2 = new ProductPropertyRequest();
        propertyRequest2.setPropertyId(2);
        propertyRequest2.setValue("Property Value 2");

        productRequest.setProperties(Arrays.asList(propertyRequest1, propertyRequest2));

        // Создание ожидаемого объекта Product
        Product expectedProduct = new Product();
        expectedProduct.setName("Test Product");
        expectedProduct.setBrand("Test Brand");
        expectedProduct.setCount(10);
        expectedProduct.setCountType("units");
        expectedProduct.setDescription("This is a test product.");
        expectedProduct.setArticle("12345");
        expectedProduct.setMainPhotoUrl("path/to/mainPhoto.jpg");
        expectedProduct.setAdditionalPhotos(Arrays.asList("path/to/additionalPhoto1.jpg", "path/to/additionalPhoto2.jpg"));
        Category category = new Category();
        category.setId(1);
        expectedProduct.setCategory(category);
        Price price = new Price();
        price.setDiscount(10);
        price.setValue(100);
        expectedProduct.setPrice(price);
        List<ProductProperty> properties = Arrays.asList(
                new ProductProperty(1, expectedProduct, new Property(1, "Property 1", null), "Property Value 1"),
                new ProductProperty(2, expectedProduct, new Property(2, "Property 2", null), "Property Value 2")
        );
        expectedProduct.setProperties(properties);

        // Настройка мок-объекта
        when(productService.saveProduct(any(ProductRequest.class))).thenReturn(expectedProduct);

        // Выполнение запроса
        mockMvc.perform(multipart("/products")
                        .file(mainPhoto)
                        .file(additionalPhoto1)
                        .file(additionalPhoto2)
                        .param("name", "Test Product")
                        .param("brand", "Test Brand")
                        .param("count", "10")
                        .param("countType", "units")
                        .param("description", "This is a test product.")
                        .param("article", "12345")
                        .param("categoryId", "1")
                        .param("price.discount", "10")
                        .param("price.value", "100")
                        .param("properties[0].propertyId", "1")
                        .param("properties[0].value", "Property Value 1")
                        .param("properties[1].propertyId", "2")
                        .param("properties[1].value", "Property Value 2")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProduct)));
    }
}