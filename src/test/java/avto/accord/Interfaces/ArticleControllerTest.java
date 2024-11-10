package avto.accord.Interfaces;

import avto.accord.App.Domain.Models.Article.Article;
import avto.accord.App.Domain.Models.Article.ArticleRequest;
import avto.accord.App.Domain.Services.ArticleService.ArticleService;
import avto.accord.App.Web.Controllers.ArticleController.ArticleController;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
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

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllArticles() throws Exception {
        List<Article> articles = Arrays.asList(
                new Article(1, "Article 1", "photo1.jpg", "Description 1"),
                new Article(2, "Article 2", "photo2.jpg", "Description 2")
        );

        when(articleService.getAllArticles()).thenReturn(articles);

        mockMvc.perform(get("/articles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(articles)));
    }

    @Test
    public void testGetArticleById() throws Exception {
        int articleId = 1;
        Article expectedArticle = new Article(articleId, "Article 1", "photo1.jpg", "Description 1");

        when(articleService.getArticleById(anyInt())).thenReturn(Optional.of(expectedArticle));

        mockMvc.perform(get("/articles/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedArticle)));
    }

    @Test
    public void testCreateArticle() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setTitle("New Article");
        articleRequest.setDescription("New Description");

        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "photo".getBytes());
        articleRequest.setPhoto(photo);

        Article createdArticle = new Article(1, "New Article", "photo.jpg", "New Description");

        when(articleService.createArticle(any(ArticleRequest.class))).thenReturn(createdArticle);

        mockMvc.perform(multipart("/articles")
                        .file(photo)
                        .param("title", "New Article")
                        .param("description", "New Description")
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

        MockMultipartFile photo = new MockMultipartFile("photo", "updatedPhoto.jpg", MediaType.IMAGE_JPEG_VALUE, "updatedPhoto".getBytes());
        articleRequest.setPhoto(photo);

        Article updatedArticle = new Article(articleId, "Updated Article", "updatedPhoto.jpg", "Updated Description");

        when(articleService.updateArticle(anyInt(), any(ArticleRequest.class))).thenReturn(updatedArticle);

        mockMvc.perform(multipart("/articles/{id}", articleId)
                        .file(photo)
                        .param("title", "Updated Article")
                        .param("description", "Updated Description")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedArticle)));
    }

    @Test
    public void testDeleteArticle() throws Exception {
        int articleId = 1;

        mockMvc.perform(delete("/articles/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
