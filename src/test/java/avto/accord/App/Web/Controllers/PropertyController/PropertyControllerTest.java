package avto.accord.App.Web.Controllers.PropertyController;

import avto.accord.App.Domain.Models.ProductProperty.DeletePropertyValueRequest;
import avto.accord.App.Domain.Models.ProductProperty.ProductPropertyRequest;
import avto.accord.App.Domain.Models.ProductProperty.PropertyValueDTO;
import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Models.Property.PropertyDTO;
import avto.accord.App.Domain.Models.Property.PropertyRequest;
import avto.accord.App.Infrastructure.Components.Photos.PhotoStorage;
import avto.accord.App.Domain.Services.PropertyService.PropertyService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropertyController.class)
public class PropertyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PhotoStorage photoStorage;

    @MockBean
    private PropertyService propertyService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testAddPropertyValue() throws Exception {
        // Arrange
        ProductPropertyRequest request = new ProductPropertyRequest(1, "Blue");
        PropertyDTO propertyDTO = new PropertyDTO(1, "Color", Arrays.asList(new PropertyValueDTO(1, "Red"), new PropertyValueDTO(2, "Blue")));
        when(propertyService.addPropertyValue(any(ProductPropertyRequest.class))).thenReturn(propertyDTO);

        // Act
        mockMvc.perform(post("/properties/add-value")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(propertyDTO)));

        // Assert
        verify(propertyService, times(1)).addPropertyValue(any(ProductPropertyRequest.class));
    }
    @Test
    public void testDeletePropertyValue() throws Exception {
        // Arrange
        DeletePropertyValueRequest request = new DeletePropertyValueRequest(1, 1);
        PropertyDTO propertyDTO = new PropertyDTO(1, "Color", Arrays.asList(new PropertyValueDTO(2, "Blue")));
        when(propertyService.deletePropertyValue(any(DeletePropertyValueRequest.class))).thenReturn(propertyDTO);

        // Act
        mockMvc.perform(delete("/properties/delete-value")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(propertyDTO)));

        // Assert
        verify(propertyService, times(1)).deletePropertyValue(any(DeletePropertyValueRequest.class));
    }
    @Test
    public void testGetPropertyById() throws Exception {
        // Arrange
        int propertyId = 1;
        PropertyDTO propertyDTO = new PropertyDTO(1, "Color", Arrays.asList(new PropertyValueDTO(1, "Red")));
        when(propertyService.getPropertyById(propertyId)).thenReturn(propertyDTO);

        // Act
        mockMvc.perform(get("/properties/{id}", propertyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(propertyDTO)));

        // Assert
        verify(propertyService, times(1)).getPropertyById(propertyId);
    }
    @Test
    public void testAddProperty() throws Exception {
        // Arrange
        PropertyRequest propertyRequest = new PropertyRequest();
        propertyRequest.setName("Test Property");
        Property createdProperty = new Property();
        createdProperty.setId(1);
        createdProperty.setName("Test Property");

        when(propertyService.saveProperty(any(PropertyRequest.class))).thenReturn(createdProperty);

        // Act
        mockMvc.perform(post("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(propertyRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(createdProperty)));

        // Assert
        verify(propertyService, times(1)).saveProperty(any(PropertyRequest.class));
    }
    @Test
    public void testDeleteProperty() throws Exception {
        // Arrange
        int propertyId = 1;

        doNothing().when(propertyService).deleteProperty(propertyId);

        // Act
        mockMvc.perform(delete("/properties/{id}", propertyId))
                .andExpect(status().isOk());

        // Assert
        verify(propertyService, times(1)).deleteProperty(propertyId);
    }
}
