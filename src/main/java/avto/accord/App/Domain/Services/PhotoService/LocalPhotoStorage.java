package avto.accord.App.Domain.Services.PhotoService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.stream.Stream;


@Component
public class LocalPhotoStorage implements PhotoStorage {

    @Value("${photo.storage.path}")
    private String photoStoragePath;
    private final Path root;

    public LocalPhotoStorage(@Value("${photo.storage.path}") String photoStoragePath) {
        this.photoStoragePath = photoStoragePath;
        this.root = Paths.get(photoStoragePath);
    }


    /**
     * Инициализация хранилища фотографий.
     */
    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Сохранение фотографии.
     *
     * @param file файл фотографии
     */
    @Override
    public void savePhoto(MultipartFile file, String fileName) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(fileName));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("file already exists");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Загрузка фотографии.
     *
     * @param filename имя файла фотографии
     * @return ресурс фотографии
     */
    @Override
    public Resource loadPhoto(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    /**
     * Загрузка всех фотографий.
     *
     * @return поток путей к фотографиям
     */
    @Override
    public Stream<Path> loadAllPhotos() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
