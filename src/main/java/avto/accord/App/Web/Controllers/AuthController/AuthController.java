package avto.accord.App.Web.Controllers.AuthController;

import avto.accord.App.Domain.Models.LoginRequest.LoginRequest;
import avto.accord.App.Domain.Services.ApiKeyService.ApiKeyService;
import avto.accord.App.Infrastructure.Annotations.PublicEndpoint.PublicEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final ApiKeyService apiKeyService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @PostMapping("/token")
    @PublicEndpoint
    public ResponseEntity<Map<String, String>> getToken(@RequestBody LoginRequest loginRequest) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                log.warn("Failed login attempt for username: {}", loginRequest.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid username or password"));
            }

            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElseThrow(() -> new RuntimeException("User has no roles"));
            String apiKey = apiKeyService.generateApiKey(role);

            log.info("Generated API key for username: {}, role: {}", loginRequest.getUsername(), role);
            return ResponseEntity.ok(Map.of("apiKey", apiKey));
        } catch (UsernameNotFoundException ex) {
            log.warn("User not found: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }
}
