package avto.accord.App.Domain.Services.PhotoService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {
    @Value("${photo.storage.path}")
    private String photoStoragePath;

    public void savePhoto(String photoPath, byte[] photoData) throws IOException {
        Path path = Paths.get(photoStoragePath, photoPath);
        Files.createDirectories(path.getParent());
        Files.write(path, photoData);
    }

    public byte[] getPhoto(String photoPath) throws IOException {
        Path path = Paths.get(photoStoragePath, photoPath);
        return Files.readAllBytes(path);
    }

    public List<byte[]> getPhotos(List<String> photoPaths) {
        return photoPaths.stream()
                .map(this::getPhotoWrapper)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<byte[]> getPhotoWrapper(String photoPath) {
        try {
            return Optional.of(getPhoto(photoPath));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
