package avto.accord.App.Domain.Services.ApiKeyService;

import avto.accord.App.Domain.Models.ApiKey.ApiKey;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {

    private ApiKey adminApiKey; // Храним единственный токен для администратора

    /**
     * Генерация нового токена для администратора
     */
    public String generateAdminApiKey() {
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1); // Токен действителен 1 час
        adminApiKey = new ApiKey(token, "ADMIN", expirationTime);
        return token;
    }

    /**
     * Валидация токена
     */
    public boolean validateApiKey(String apiKey) {
        if (adminApiKey == null || !adminApiKey.getToken().equals(apiKey)) {
            return false;
        }
        return adminApiKey.getExpirationTime().isAfter(LocalDateTime.now()); // Проверка срока действия
    }

    /**
     * Получение роли по токену
     */
    public Optional<String> getRoleForApiKey(String apiKey) {
        if (validateApiKey(apiKey)) {
            return Optional.of(adminApiKey.getRole());
        }
        return Optional.empty();
    }

    /**
     * Проверка существования активного токена
     */
    public boolean hasActiveToken() {
        return adminApiKey != null && adminApiKey.getExpirationTime().isAfter(LocalDateTime.now());
    }

    /**
     * Отзыв токена (логаут)
     */
    public void invalidateToken() {
        adminApiKey = null; // Удаляем активный токен
    }
}