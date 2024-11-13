package avto.accord.App.Web.Controllers.PropertyController;

import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Models.Property.PropertyRequest;
import avto.accord.App.Domain.Services.PhotoService.PhotoStorage;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void testAddProperty() throws Exception {
        PropertyRequest propertyRequest = new PropertyRequest();
        propertyRequest.setName("Test Property");
        Property createdProperty = new Property();
        createdProperty.setId(1);
        createdProperty.setName("Test Property");

        when(propertyService.saveProperty(any(PropertyRequest.class))).thenReturn(createdProperty);

        mockMvc.perform(post("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(propertyRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(createdProperty)));
    }
    @Test
    public void testDeleteProperty() throws Exception {
        int propertyId = 1;

        doNothing().when(propertyService).deleteProperty(propertyId);

        mockMvc.perform(delete("/properties/{id}", propertyId))
                .andExpect(status().isOk());
    }
}
