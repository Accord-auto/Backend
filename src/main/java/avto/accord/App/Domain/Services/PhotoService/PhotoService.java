package avto.accord.App.Domain.Services.PhotoService;

import avto.accord.App.Domain.Models.FileInfo.FileInfo;
import avto.accord.App.Infrastructure.Components.Photos.PhotoStorage;
import avto.accord.App.Web.Controllers.PhotoContoller.PhotoController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void savePhoto(MultipartFile file, String fileName) {
        photoStorage.savePhoto(file, fileName);
    }

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
    public List<Resource> getPhotos(List<String> photoNames) throws IOException {
        return photoNames.stream()
                .map(photoName -> {
                    try {
                        return getPhoto(photoName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
