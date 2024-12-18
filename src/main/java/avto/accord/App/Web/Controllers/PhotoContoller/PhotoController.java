package avto.accord.App.Web.Controllers.PhotoContoller;

import avto.accord.App.Domain.Models.FileInfo.FileInfo;
import avto.accord.App.Domain.Services.PhotoService.PhotoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/photos")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @GetMapping("/")
    public ResponseEntity<List<FileInfo>> getListPhotos() {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.getPhotos());
    }

    @GetMapping("/{photoname}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String photoname) {
        try {
            Resource photo = photoService.getPhoto(photoname);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + photo.getFilename() + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(photo);
        } catch (Exception e) {
            log.error("Error retrieving photo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public void getPhotos(@RequestParam List<String> photoNames, HttpServletResponse response) {
        try {
            List<Resource> photos = photoService.getPhotos(photoNames);

            response.setContentType(MediaType.MULTIPART_MIXED_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"photos\"");

            for (Resource photo : photos) {
                response.getOutputStream().write(photo.getInputStream().readAllBytes());
            }
        } catch (IOException e) {
            log.error("Error retrieving photos", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}