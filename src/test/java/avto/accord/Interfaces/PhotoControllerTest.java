package avto.accord.Interfaces;

import avto.accord.App.Domain.Services.PhotoService.PhotoService;
import avto.accord.App.Web.Controllers.PhotoContoller.PhotoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhotoController.class)
public class PhotoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPhoto() throws Exception {
        String photoPath = "photo1.jpg";
        byte[] photoData = "photo1".getBytes();

        when(photoService.getPhoto(any(String.class))).thenReturn(photoData);

        mockMvc.perform(get("/photos/{photoPath}", photoPath)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk())
                .andExpect(content().bytes(photoData));
    }

    @Test
    public void testGetPhotos() throws Exception {
        List<String> photoPaths = Arrays.asList("photo1.jpg", "photo2.jpg");
        List<byte[]> photoDataList = Arrays.asList("photo1".getBytes(), "photo2".getBytes());
        when(photoService.getPhotos(photoPaths)).thenReturn(photoDataList);
        mockMvc.perform(get("/photos/batch")
                        .param("photoPaths", "photo1.jpg")
                        .param("photoPaths", "photo2.jpg")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"cGhvdG8x\", \"cGhvdG8y\"]")); // Base64-encoded strings
    }

    @Test
    public void testGetPhotoNotFound() throws Exception {
        String photoPath = "photo1.jpg";

        when(photoService.getPhoto(any(String.class))).thenThrow(new IOException("Photo not found"));

        mockMvc.perform(get("/photos/{photoPath}", photoPath)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isNotFound());
    }
}
