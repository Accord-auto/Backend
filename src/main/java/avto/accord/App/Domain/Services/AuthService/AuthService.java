package avto.accord.App.Domain.Services.AuthService;

import avto.accord.App.Domain.Services.ApiKeyService.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final ApiKeyService apiKeyService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Аутентификация пользователя и генерация токена
     */
    public Optional<String> authenticate(String username, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                return Optional.empty(); // Неверные учетные данные
            }

            // Проверяем, есть ли уже активный токен
            if (apiKeyService.hasActiveToken()) {
                return Optional.of("Token already exists and is active");
            }

            // Генерация нового токена для администратора
            String apiKey = apiKeyService.generateAdminApiKey();
            return Optional.of(apiKey);
        } catch (Exception ex) {
            return Optional.empty(); // Пользователь не найден или другая ошибка
        }
    }
}