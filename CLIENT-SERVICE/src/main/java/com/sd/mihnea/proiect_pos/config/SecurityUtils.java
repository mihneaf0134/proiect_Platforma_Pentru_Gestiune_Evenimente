package com.sd.mihnea.proiect_pos.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class SecurityUtils {

    public static void requireRole(HttpServletRequest request, List<String> roles) {

        String role = (String) request.getAttribute("role");

        if (role == null || !roles.contains(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for role: " + role);
        }
    }

    public static Integer getUserId(HttpServletRequest request) {
        return (Integer) request.getAttribute("idOwner");
    }

    public static String getEmail(HttpServletRequest request) {
        return (String) request.getAttribute("email");
    }
}
