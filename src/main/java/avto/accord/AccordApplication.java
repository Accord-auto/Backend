package avto.accord;

import avto.accord.App.Infrastructure.Components.Photos.PhotoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccordApplication implements CommandLineRunner {
    @Autowired
    private PhotoStorage storageService;

    public static void main(String[] args) {
        SpringApplication.run(AccordApplication.class, args);
    }

    /**
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) {
        try {
            storageService.init();
        } catch (Exception e) {
            System.err.println("Error initializing photo storage: " + e.getMessage());
        }
    }
}
