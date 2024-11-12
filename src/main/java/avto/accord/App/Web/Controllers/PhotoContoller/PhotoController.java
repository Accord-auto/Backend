package avto.accord.App.Web.Controllers.PhotoContoller;

import avto.accord.App.Domain.Models.FileInfo.FileInfo;
import avto.accord.App.Domain.Services.PhotoService.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;

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

    @GetMapping("/{photoname:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getPhoto(@PathVariable String photoname) throws IOException {
        Resource photo = photoService.getPhoto(photoname);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; photoname=\"" + photo.getFilename() + "\"").body(photo);
    }
}
