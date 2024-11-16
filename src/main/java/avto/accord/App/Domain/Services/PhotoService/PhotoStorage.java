package avto.accord.App.Domain.Services.PhotoService;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface PhotoStorage {
    void init();
    void savePhoto(MultipartFile file, String fileName);
    Resource loadPhoto(String filename);
    Stream<Path> loadAllPhotos();
}
