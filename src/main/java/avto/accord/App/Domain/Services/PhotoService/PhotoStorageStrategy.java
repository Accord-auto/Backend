package avto.accord.App.Domain.Services.PhotoService;

import java.io.IOException;

public interface PhotoStorageStrategy {
    void savePhoto(String photoPath, byte[] photoData) throws IOException;
    byte[] getPhoto(String photoPath) throws IOException;
}
