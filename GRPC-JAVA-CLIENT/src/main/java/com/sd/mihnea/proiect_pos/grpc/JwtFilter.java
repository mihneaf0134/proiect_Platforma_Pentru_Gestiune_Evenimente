package com.sd.mihnea.proiect_pos.grpc;

import com.sd.mihnea.grpcdemo.auth.ValidateResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final GrpcAuthClient grpcAuthClient;

    public JwtFilter(GrpcAuthClient grpcAuthClient) {
        this.grpcAuthClient = grpcAuthClient;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        return path.equals("/auth/login") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-resources") ||
                path.equals("api/event-manager/events/") ||
                path.equals("api/event-manager/event-packets/");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = header.substring(7);
        ValidateResponse res = grpcAuthClient.validate(token);

        if (!res.getSuccess()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        request.setAttribute("idOwner", res.getSub());
        request.setAttribute("role", res.getRole());
        request.setAttribute("email", res.getEmail());


        filterChain.doFilter(request, response);
    }
}
