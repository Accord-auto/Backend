package avto.accord.App.Domain.Models.ApiKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiKey {
    private String token;
    private String role;
    private LocalDateTime expirationTime;
}
