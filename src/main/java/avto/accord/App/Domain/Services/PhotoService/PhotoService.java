package avto.accord.App.Domain.Services.PhotoService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoStorageStrategy photoStorageStrategy;

    public void savePhoto(String photoPath, byte[] photoData) throws IOException {
        photoStorageStrategy.savePhoto(photoPath, photoData);
    }
    @Cacheable(value = "photos", key = "#photoPath")
    public byte[] getPhoto(String photoPath) throws IOException {
        return photoStorageStrategy.getPhoto(photoPath);
    }
    @Async
    public CompletableFuture<List<byte[]>> getPhotosAsync(List<String> photoPaths) {
        return CompletableFuture.supplyAsync(() -> photoPaths.stream()
                .map(this::getPhotoWrapper)
                .flatMap(Optional::stream)
                .collect(Collectors.toList()));
    }

    private Optional<byte[]> getPhotoWrapper(String photoPath) {
        try {
            return Optional.of(getPhoto(photoPath));
        } catch (IOException e) {
            log.error("Error reading photo: {}", photoPath, e);
            return Optional.empty();
        }
    }
}
