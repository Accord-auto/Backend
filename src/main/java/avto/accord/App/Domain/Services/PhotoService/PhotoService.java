package avto.accord.App.Domain.Services.PhotoService;

import avto.accord.App.Domain.Models.FileInfo.FileInfo;
import avto.accord.App.Web.Controllers.PhotoContoller.PhotoController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {
    @Autowired
    private PhotoStorage photoStorage;

    public void savePhoto(MultipartFile file) {
        photoStorage.savePhoto(file);
    }
    @Cacheable(value = "photos", key = "#photoname")
    public Resource getPhoto(String photoname) throws IOException {
        return photoStorage.loadPhoto(photoname);
    }

    public List<FileInfo> getPhotos() {
        return photoStorage.loadAllPhotos().map(path -> {
            String photoName = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(PhotoController.class, "getPhoto", path.getFileName().toString()).build().toString();

            return new FileInfo(photoName, url);
        }).collect(Collectors.toList());
    }
}
