package avto.accord.Interfaces;

import avto.accord.App.Domain.Models.Category.Category;
import avto.accord.App.Domain.Models.Price.Price;
import avto.accord.App.Domain.Models.Price.PriceRequest;
import avto.accord.App.Domain.Models.Product.Product;
import avto.accord.App.Domain.Models.Product.ProductRequestPayload;
import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Services.PhotoService.PhotoStorage;
import avto.accord.App.Domain.Services.ProductRequestService.ProductRequestService;
import avto.accord.App.Domain.Services.ProductService.ProductService;
import avto.accord.App.Web.Controllers.ProductController.ProductController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRequestService productRequestService;

    @MockBean
    private PhotoStorage photoStorage;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetAllProducts() throws Exception {
        int offset = 0;
        int limit = 10;

        List<Product> products = Arrays.asList(
                createProduct(1, "Engine Oil", "Castrol", 50, "liters", "High-quality engine oil", "E12345", "path/to/mainPhoto1.jpg", Arrays.asList("path/to/additionalPhoto1.jpg"), 1, 100, 5, 1, "Viscosity", "5W-30"),
                createProduct(2, "Brake Pads", "Bosch", 100, "units", "High-performance brake pads", "B67890", "path/to/mainPhoto2.jpg", Arrays.asList("path/to/additionalPhoto2.jpg"), 2, 200, 10, 2, "Material", "Ceramic")
        );

        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(offset, limit), products.size());

        when(productService.getAllProducts(anyInt(), anyInt())).thenReturn(productPage);

        mockMvc.perform(get("/products")
                        .param("offset", String.valueOf(offset))
                        .param("limit", String.valueOf(limit))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productPage)));
    }

    private Product createProduct(int id, String name, String brand, int count, String countType, String description, String article, String mainPhotoUrl, List<String> additionalPhotos, int categoryId, int priceValue, int priceDiscount, int propertyId, String propertyName, String propertyValue) {
        Category category = new Category(categoryId);
        Price price = new Price(priceValue, priceDiscount);
        Property property = new Property(propertyId, propertyName, null);
        ProductProperty productProperty = new ProductProperty(id, new Product(), property, propertyValue);

        return new Product(id, name, brand, count, countType, description, article, mainPhotoUrl, additionalPhotos, category, price, Arrays.asList(productProperty));
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
        ProductRequestPayload productRequestPayload = new ProductRequestPayload();
        productRequestPayload.setName("Engine Oil");
        productRequestPayload.setBrand("Castrol");
        productRequestPayload.setCount(50);
        productRequestPayload.setCountType("liters");
        productRequestPayload.setDescription("High-quality engine oil");
        productRequestPayload.setArticle("E12345");
        productRequestPayload.setCategoryId(1);
        productRequestPayload.setSpecialOffer(true);
        productRequestPayload.setCustomerArticle("CUST123");

        // Создание мок-файлов для фотографий
        MockMultipartFile mainPhoto = new MockMultipartFile("mainPhoto", "mainPhoto.jpg", MediaType.IMAGE_JPEG_VALUE, "mainPhoto".getBytes());
        MockMultipartFile additionalPhoto1 = new MockMultipartFile("additionalPhotos", "additionalPhoto1.jpg", MediaType.IMAGE_JPEG_VALUE, "additionalPhoto1".getBytes());
        MockMultipartFile additionalPhoto2 = new MockMultipartFile("additionalPhotos", "additionalPhoto2.jpg", MediaType.IMAGE_JPEG_VALUE, "additionalPhoto2".getBytes());

        // Создание мок-объекта для цены
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setDiscount(5);
        priceRequest.setValue(100);
        productRequestPayload.setPrice(priceRequest);

        // Создание мок-объекта для свойств
        ProductPropertyRequest propertyRequest1 = new ProductPropertyRequest();
        propertyRequest1.setPropertyId(1);
        propertyRequest1.setValue("5W-30");

        ProductPropertyRequest propertyRequest2 = new ProductPropertyRequest();
        propertyRequest2.setPropertyId(2);
        propertyRequest2.setValue("High-quality");

        productRequestPayload.setProperties(Arrays.asList(propertyRequest1, propertyRequest2));

        Product expectedProduct = new Product();
        expectedProduct.setName("Engine Oil");
        expectedProduct.setBrand("Castrol");
        expectedProduct.setCount(50);
        expectedProduct.setCountType("liters");
        expectedProduct.setDescription("High-quality engine oil");
        expectedProduct.setArticle("E12345");
        expectedProduct.setMainPhotoUrl("mainPhoto.jpg");
        expectedProduct.setAdditionalPhotos(Arrays.asList("additionalPhoto1.jpg", "additionalPhoto2.jpg"));
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
        expectedProduct.setSpecialOffer(true);
        expectedProduct.setCustomerArticle("CUST123");

        // Настройка мок-объекта
        when(productRequestService.createProduct(any(MultipartFile.class), anyList(), anyString())).thenReturn(expectedProduct);

        // Выполнение запроса
        mockMvc.perform(multipart("/products")
                        .file(mainPhoto)
                        .file(additionalPhoto1)
                        .file(additionalPhoto2)
                        .file(new MockMultipartFile("productRequestPayload", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(productRequestPayload).getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProduct)));
    }

    @Test
    public void testUpdateProductPrice() throws Exception {
        int productId = 1;
        int newPrice = 200;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setPrice(new Price(newPrice, 0));

        when(productService.updatePrice(anyInt(), anyInt())).thenReturn(expectedProduct);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}/price", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPrice)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProduct)));
    }
    @Test
    public void testUpdateCustomerArticle() throws Exception {
        int productId = 1;
        String newCustomerArticle = "NEWCUST123";

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setCustomerArticle(newCustomerArticle);

        when(productService.updateCustomerArticle(productId, newCustomerArticle)).thenReturn(updatedProduct);

        mockMvc.perform(put("/products/{id}/customerArticle", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCustomerArticle))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedProduct)));
    }
    @Test
    public void testUpdateProductDiscount() throws Exception {
        int productId = 1;
        int newDiscount = 20;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setPrice(new Price(0, newDiscount));

        when(productService.updateDiscount(anyInt(), anyInt())).thenReturn(expectedProduct);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}/discount", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDiscount)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProduct)));
    }

    @Test
    public void testUpdateProductCount() throws Exception {
        int productId = 1;
        int newCount = 20;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setCount(newCount);

        when(productService.updateCount(anyInt(), anyInt())).thenReturn(expectedProduct);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}/count", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCount)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProduct)));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        int productId = 1;

        when(productService.deleteProduct(anyInt())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}