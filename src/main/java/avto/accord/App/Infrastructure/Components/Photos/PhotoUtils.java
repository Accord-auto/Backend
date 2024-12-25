package avto.accord.App.Infrastructure.Components.Photos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class PhotoUtils {
    private final PhotoStorage photoStorage;

    @Autowired
    public PhotoUtils(PhotoStorage photoStorage) {
        this.photoStorage = photoStorage;
    }

    public String savePhoto(MultipartFile photo) throws IOException {
        String photoPath = UUID.randomUUID().toString() + getFileExtension(photo.getOriginalFilename());
        photoStorage.savePhoto(photo, photoPath);
        return photoPath;
    }

    public static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex);
    }
}
