package avto.accord.App.Infrastructure.Components.Photos;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface PhotoStorage {
    void init();
    void savePhoto(MultipartFile file, String fileName);
    Resource loadPhoto(String filename);
    Stream<Path> loadAllPhotos();
    void deletePhoto(String filename);
}
