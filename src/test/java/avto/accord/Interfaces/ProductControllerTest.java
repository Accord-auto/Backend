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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void testGetAllProducts() throws Exception {
        int offset = 0;
        int limit = 10;
        try {
            List<Product> products = Arrays.asList(
                    new Product(1, "Engine Oil", "Castrol", 50, "liters", "High-quality engine oil", "E12345", "path/to/mainPhoto1.jpg", Arrays.asList("path/to/additionalPhoto1.jpg"),
                            new Category(1), new Price(100, 5), Arrays.asList(
                            new ProductProperty(1, new Product(1, "Engine Oil", "Castrol", 50, "liters", "High-quality engine oil", "E12345", "path/to/mainPhoto1.jpg", Arrays.asList("path/to/additionalPhoto1.jpg"),
                                    new Category(1),
                                    new Price(100, 5), Arrays.asList()), new Property(1, "Viscosity", null), "5W-30"))),
                    new Product(2, "Brake Pads", "Bosch", 100, "units", "High-performance brake pads", "B67890", "path/to/mainPhoto2.jpg", Arrays.asList("path/to/additionalPhoto2.jpg"),
                            new Category(2),
                            new Price(200, 10), Arrays.asList(
                            new ProductProperty(2,
                                    new Product(2, "Brake Pads", "Bosch", 100, "units", "High-performance brake pads", "B67890", "path/to/mainPhoto2.jpg", Arrays.asList("path/to/additionalPhoto2.jpg"),
                                            new Category(2), new Price(200, 10), Arrays.asList()),
                                    new Property(2, "Material", null), "Ceramic")))
            );
            Page<Product> productPage = new PageImpl<>(products, PageRequest.of(offset, limit), products.size());

            when(productService.getAllProducts(anyInt(), anyInt())).thenReturn(productPage);
            mockMvc.perform(get("/products")
                            .param("offset", String.valueOf(offset))
                            .param("limit", String.valueOf(limit))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(productPage)));
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testGetProductById() throws Exception {
        int productId = 1;
        Product expectedProduct = new Product(1, "Product 1", "Brand 1", 10, "units", "Description 1", "12345", "path/to/mainPhoto1.jpg", Arrays.asList("path/to/additionalPhoto1.jpg"),
                new Category(1), new Price(100, 10),
                Arrays.asList(
                        new ProductProperty(
                                1, new Product(1, "Engine Oil", "Castrol", 50, "liters", "High-quality engine oil", "E12345", "path/to/mainPhoto1.jpg", Arrays.asList("path/to/additionalPhoto1.jpg"),
                                new Category(1), new Price(100, 5), Arrays.asList()),
                                new Property(1, "Viscosity", null), "5W-30")));

        when(productService.getProductById(anyInt())).thenReturn(expectedProduct);

        mockMvc.perform(get("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProduct)));
    }


    @Test
    public void testCreateProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Engine Oil");
        productRequest.setBrand("Castrol");
        productRequest.setCount(50);
        productRequest.setCountType("liters");
        productRequest.setDescription("High-quality engine oil");
        productRequest.setArticle("E12345");
        productRequest.setCategoryId(1);

        // Создание мок-файлов для фотографий
        MockMultipartFile mainPhoto = new MockMultipartFile("mainPhoto", "mainPhoto.jpg", MediaType.IMAGE_JPEG_VALUE, "mainPhoto".getBytes());
        MockMultipartFile additionalPhoto1 = new MockMultipartFile("additionalPhotos", "additionalPhoto1.jpg", MediaType.IMAGE_JPEG_VALUE, "additionalPhoto1".getBytes());
        MockMultipartFile additionalPhoto2 = new MockMultipartFile("additionalPhotos", "additionalPhoto2.jpg", MediaType.IMAGE_JPEG_VALUE, "additionalPhoto2".getBytes());

        productRequest.setMainPhoto(mainPhoto);
        productRequest.setAdditionalPhotos(Arrays.asList(additionalPhoto1, additionalPhoto2));

        // Создание мок-объекта для цены
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setDiscount(5);
        priceRequest.setValue(100);
        productRequest.setPrice(priceRequest);

        // Создание мок-объекта для свойств
        ProductPropertyRequest propertyRequest1 = new ProductPropertyRequest();
        propertyRequest1.setPropertyId(1);
        propertyRequest1.setValue("5W-30");

        ProductPropertyRequest propertyRequest2 = new ProductPropertyRequest();
        propertyRequest2.setPropertyId(2);
        propertyRequest2.setValue("High-quality");

        productRequest.setProperties(Arrays.asList(propertyRequest1, propertyRequest2));

        Product expectedProduct = new Product();
        expectedProduct.setName("Engine Oil");
        expectedProduct.setBrand("Castrol");
        expectedProduct.setCount(50);
        expectedProduct.setCountType("liters");
        expectedProduct.setDescription("High-quality engine oil");
        expectedProduct.setArticle("E12345");
        expectedProduct.setMainPhotoUrl("path/to/mainPhoto.jpg");
        expectedProduct.setAdditionalPhotos(Arrays.asList("path/to/additionalPhoto1.jpg", "path/to/additionalPhoto2.jpg"));
        Category category = new Category();
        category.setId(1);
        expectedProduct.setCategory(category);
        Price price = new Price();
        price.setDiscount(5);
        price.setValue(100);
        expectedProduct.setPrice(price);
        List<ProductProperty> properties = Arrays.asList(
                new ProductProperty(1, expectedProduct, new Property(1, "Viscosity", null), "5W-30"),
                new ProductProperty(2, expectedProduct, new Property(2, "Quality", null), "High-quality")
        );
        expectedProduct.setProperties(properties);

        // Настройка мок-объекта
        when(productService.saveProduct(any(ProductRequest.class))).thenReturn(expectedProduct);

        // Выполнение запроса
        mockMvc.perform(multipart("/products")
                        .file(mainPhoto)
                        .file(additionalPhoto1)
                        .file(additionalPhoto2)
                        .param("name", "Engine Oil")
                        .param("brand", "Castrol")
                        .param("count", "50")
                        .param("countType", "liters")
                        .param("description", "High-quality engine oil")
                        .param("article", "E12345")
                        .param("categoryId", "1")
                        .param("price.discount", "5")
                        .param("price.value", "100")
                        .param("properties[0].propertyId", "1")
                        .param("properties[0].value", "5W-30")
                        .param("properties[1].propertyId", "2")
                        .param("properties[1].value", "High-quality")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProduct)));
    }

    @Test
    public void testUpdatePrice() throws Exception {
        int productId = 1;
        int newPrice = 150;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setPrice(new Price(newPrice, 10));

        when(productService.updatePrice(anyInt(), anyInt())).thenReturn(expectedProduct);

        mockMvc.perform(put("/products/{id}/price", productId)
                        .param("newPrice", String.valueOf(newPrice))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProduct)));
    }

    @Test
    public void testUpdateDiscount() throws Exception {
        int productId = 1;
        int newDiscount = 20;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setPrice(new Price(100, newDiscount));

        when(productService.updateDiscount(anyInt(), anyInt())).thenReturn(expectedProduct);

        mockMvc.perform(put("/products/{id}/discount", productId)
                        .param("newDiscount", String.valueOf(newDiscount))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProduct)));
    }

    @Test
    public void testUpdateCount() throws Exception {
        int productId = 1;
        int newCount = 20;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setCount(newCount);

        when(productService.updateCount(anyInt(), anyInt())).thenReturn(expectedProduct);

        mockMvc.perform(put("/products/{id}/count", productId)
                        .param("newCount", String.valueOf(newCount))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProduct)));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        int productId = 1;

        doNothing().when(productService).deleteProduct(anyInt());

        mockMvc.perform(delete("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}