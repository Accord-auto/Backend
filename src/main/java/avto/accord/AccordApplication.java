package avto.accord;

import avto.accord.App.Infrastructure.Components.Photos.PhotoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccordApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccordApplication.class, args);
    }

}
