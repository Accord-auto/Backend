package avto.accord.App.Infrastructure.Components.ApiKeyFilter;

import avto.accord.App.Domain.Services.ApiKeyService.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        log.info("Processing request for path: {}", path);

        if (path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs") || path.startsWith("/v3-docs")) {
            log.debug("Skipping API key validation for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        // Проверяем наличие API-ключа в заголовке
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null) {
            log.warn("Missing X-API-KEY header for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Validating API key: {}", apiKey);
        if (!apiKeyService.validateApiKey(apiKey)) {
            log.warn("Invalid or expired API key: {}", apiKey);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired API key");
            return;
        }

        apiKeyService.getRoleForApiKey(apiKey).ifPresent(role -> {
            log.info("Setting role in security context: {}", role);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            "api-user",
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });

        filterChain.doFilter(request, response);
    }
}