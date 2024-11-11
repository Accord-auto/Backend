package avto.accord.App.Domain.Services.PhotoService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
@RequiredArgsConstructor
public class LocalPhotoStorageStrategy implements PhotoStorageStrategy {

    @Value("${photo.storage.path}")
    private String photoStoragePath;

    @Override
    public void savePhoto(String photoPath, byte[] photoData) throws IOException {
        Path path = Paths.get(photoStoragePath, photoPath);
        Files.createDirectories(path.getParent());
        Files.write(path, photoData);
    }

    @Override
    public byte[] getPhoto(String photoPath) throws IOException {
        Path path = Paths.get(photoStoragePath, photoPath);
        return Files.readAllBytes(path);
    }
}
