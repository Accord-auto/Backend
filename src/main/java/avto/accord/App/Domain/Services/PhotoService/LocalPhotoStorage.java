package avto.accord.App.Domain.Services.PhotoService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public void savePhoto(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if(e instanceof FileSystemAlreadyExistsException){
                throw new RuntimeException("file already exists");
            }
            throw new RuntimeException(e.getMessage());
        }
    }
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
    @Override
    public Stream<Path> loadAllPhotos() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
