package avto.accord.App.Web.Controllers.ArticleController;

import avto.accord.App.Domain.Models.Article.Article;
import avto.accord.App.Domain.Models.Article.ArticleRequest;
import avto.accord.App.Domain.Services.ArticleService.ArticleService;
import avto.accord.App.Domain.Services.PhotoService.PhotoStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

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

@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    private ObjectMapper objectMapper;

    @MockBean
    private PhotoStorage photoStorage;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllArticles() throws Exception {
        List<Article> articles = Arrays.asList(
                new Article(1, "Article 1", "photo1.jpg", "Description 1"),
                new Article(2, "Article 2", "photo2.jpg", "Description 2")
        );

        when(articleService.getAllArticles()).thenReturn(articles);

        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(articles)));
    }

    @Test
    public void testGetArticleById() throws Exception {
        int articleId = 1;
        Article article = new Article(articleId, "Article 1", "photo1.jpg", "Description 1");

        when(articleService.getArticleById(articleId)).thenReturn(Optional.of(article));

        mockMvc.perform(get("/articles/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(article)));
    }

    @Test
    public void testGetArticleByIdNotFound() throws Exception {
        int articleId = 1;

        when(articleService.getArticleById(articleId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/articles/{id}", articleId))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testCreateArticle() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setTitle("New Article");
        articleRequest.setDescription("New Description");
        MockMultipartFile photo = new MockMultipartFile("Photo", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "photo".getBytes());
        MockMultipartFile articleRequestPayload = new MockMultipartFile("articleRequestPayload", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(articleRequest));
        Article createdArticle = new Article(1, "New Article", "photo.jpg", "New Description");

        when(articleService.createArticle(any(ArticleRequest.class), any(MultipartFile.class))).thenReturn(createdArticle);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/articles")
                        .file(photo)
                        .file(articleRequestPayload)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(createdArticle)));
    }

    @Test
    public void testUpdateArticle() throws Exception {
        int articleId = 1;
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setTitle("Updated Article");
        articleRequest.setDescription("Updated Description");
        MockMultipartFile newPhoto = new MockMultipartFile("Photo", "newPhoto.jpg", MediaType.IMAGE_JPEG_VALUE, "newPhoto".getBytes());
        MockMultipartFile articleRequestPayload = new MockMultipartFile("articleRequestPayload", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(articleRequest));
        Article updatedArticle = new Article(articleId, "Updated Article", "newPhoto.jpg", "Updated Description");

        when(articleService.updateArticle(anyInt(), any(ArticleRequest.class), any(MultipartFile.class))).thenReturn(updatedArticle);

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/articles/{id}", articleId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        mockMvc.perform(builder
                        .file(newPhoto)
                        .file(articleRequestPayload)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedArticle)));
    }



    @Test
    public void testDeleteArticle() throws Exception {
        int articleId = 1;

        doNothing().when(articleService).deleteArticle(articleId);

        mockMvc.perform(delete("/articles/{id}", articleId))
                .andExpect(status().isNoContent());
    }
}
