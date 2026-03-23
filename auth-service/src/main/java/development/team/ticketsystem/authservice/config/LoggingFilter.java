package development.team.ticketsystem.authservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            logRequest(request, response, duration);
        }
    }

    private void logRequest(HttpServletRequest request,
                            HttpServletResponse response,
                            long duration) {

        String method = request.getMethod();

        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String fullUri = query == null ? uri : uri + "?" + query;

        int status = response.getStatus();

        if (status >= 500) {
            log.error("{} {} -> {} ({} ms)", method, fullUri, status, duration);
        } else if (status >= 400) {
            log.warn("{} {} -> {} ({} ms)", method, fullUri, status, duration);
        } else {
            log.info("{} {} -> {} ({} ms)", method, fullUri, status, duration);
        }
    }
}
