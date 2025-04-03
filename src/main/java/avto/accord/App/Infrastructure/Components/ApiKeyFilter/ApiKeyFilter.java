package avto.accord.App.Infrastructure.Components.ApiKeyFilter;

import avto.accord.App.Domain.Services.ApiKeyService.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {
    private final ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Исключаем Swagger UI и API документацию из проверки API-ключа
        if (path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs") || path.startsWith("/v3-docs")) {
            filterChain.doFilter(request, response); // Пропускаем запрос без проверки API-ключа
            return;
        }

        // Проверяем наличие API-ключа в заголовке
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null) {
            // Если API-ключа нет, пропускаем запрос без проверки (может быть публичный метод)
            filterChain.doFilter(request, response);
            return;
        }

        if (!apiKeyService.validateApiKey(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API key");
            return;
        }

        String role = apiKeyService.getRoleForApiKey(apiKey);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(null, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}