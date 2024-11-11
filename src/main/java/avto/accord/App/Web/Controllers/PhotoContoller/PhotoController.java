package avto.accord.App.Web.Controllers.PhotoContoller;

import avto.accord.App.Domain.Services.PhotoService.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/photos")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @GetMapping("/{photoPath}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String photoPath) {
        try {
            byte[] photo = photoService.getPhoto(photoPath);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(photo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/batch")
    public ResponseEntity<List<byte[]>> getPhotos(@RequestParam List<String> photoPaths) {
        List<byte[]> photos = photoService.getPhotos(photoPaths);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(photos);
    }
}
