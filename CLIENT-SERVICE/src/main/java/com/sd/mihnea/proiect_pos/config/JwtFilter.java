package com.sd.mihnea.proiect_pos.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String AUTH_SERVICE_URL = "http://CLIENT-JAVA-GRPC:8082/auth/validate";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", header);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> res = restTemplate.exchange(
                    AUTH_SERVICE_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            Map<String, Object> body = res.getBody();
            if (body != null) {
                request.setAttribute("email", body.get("email"));
                request.setAttribute("role", body.get("role"));
                request.setAttribute("idOwner", body.get("idOwner"));
            }

        } catch (HttpClientErrorException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }
}