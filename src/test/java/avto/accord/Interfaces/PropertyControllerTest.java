package avto.accord.Interfaces;

import avto.accord.App.Domain.Models.Property.Property;
import avto.accord.App.Domain.Models.Property.PropertyRequest;
import avto.accord.App.Domain.Services.PropertyService.PropertyService;
import avto.accord.App.Web.Controllers.PropertyController.PropertyController;
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
    private PropertyService propertyService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProperty() throws Exception {
        PropertyRequest request = new PropertyRequest();
        request.setName("New Property");
        Property property = new Property(1, "New Property", null);

        when(propertyService.save(any(PropertyRequest.class))).thenReturn(property);

        mockMvc.perform(post("/properties")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(property)));
    }

    @Test
    public void testDeleteProperty() throws Exception {
        int propertyId = 1;

        doNothing().when(propertyService).delete(propertyId);

        mockMvc.perform(delete("/properties/{id}", propertyId))
                .andExpect(status().isOk());
    }
}
