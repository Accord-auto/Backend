package avto.accord.App.Web.Controllers.AuthController;

import avto.accord.App.Domain.Models.LoginRequest.LoginRequest;
import avto.accord.App.Domain.Services.ApiKeyService.ApiKeyService;
import avto.accord.App.Domain.Services.AuthService.AuthService;
import avto.accord.App.Infrastructure.Annotations.PublicEndpoint.PublicEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final ApiKeyService apiKeyService;
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
    @PostMapping("/logout")
    @PublicEndpoint
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("X-API-KEY") String apiKey) {
        if (!apiKeyService.validateApiKey(apiKey)) {
            log.warn("Invalid API key provided for logout");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid API key"));
        }

        apiKeyService.invalidateToken();
        log.info("API key invalidated successfully");
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}
