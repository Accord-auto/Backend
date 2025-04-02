package avto.accord.App.Domain.Services.ApiKeyService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ApiKeyService {

    private final Map<String, String> apiKeyStore = new HashMap<>(); // Храним ключи и их роли

    public String generateApiKey(String role) {
        String apiKey = UUID.randomUUID().toString();
        apiKeyStore.put(apiKey, role); // Привязываем ключ к роли
        return apiKey;
    }

    public boolean validateApiKey(String apiKey) {
        return apiKeyStore.containsKey(apiKey);
    }

    public String getRoleForApiKey(String apiKey) {
        return apiKeyStore.get(apiKey); // Получаем роль по ключу
    }
}