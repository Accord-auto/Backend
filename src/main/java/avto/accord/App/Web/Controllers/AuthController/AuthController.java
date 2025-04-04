package avto.accord.App.Web.Controllers.AuthController;

import avto.accord.App.Domain.Models.LoginRequest.LoginRequest;
import avto.accord.App.Domain.Services.AuthService.AuthService;
import avto.accord.App.Infrastructure.Annotations.PublicEndpoint.PublicEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/token")
    @PublicEndpoint
    public ResponseEntity<Map<String, String>> getToken(@RequestBody LoginRequest loginRequest) {
        Optional<String> apiKeyOptional = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (apiKeyOptional.isPresent()) {
            String apiKey = apiKeyOptional.get();
            if (apiKey.equals("Token already exists and is active")) {
                log.warn("Attempt to generate a new token while the existing one is still active for username: {}", loginRequest.getUsername());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "A valid token already exists"));
            }

            log.info("Generated API key for username: {}", loginRequest.getUsername());
            return ResponseEntity.ok(Map.of("apiKey", apiKey));
        }

        log.warn("Failed login attempt for username: {}", loginRequest.getUsername());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid username or password"));
    }
}
