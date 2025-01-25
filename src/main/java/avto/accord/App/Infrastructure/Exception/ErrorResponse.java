package avto.accord.App.Infrastructure.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message; // Сообщение об ошибке
    private int status; // Код статуса HTTP
    private ZonedDateTime timestamp; // Время возникновения ошибки

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value(); // Преобразуем HttpStatus в int
        this.timestamp = ZonedDateTime.now(); // Устанавливаем текущее время
    }
}