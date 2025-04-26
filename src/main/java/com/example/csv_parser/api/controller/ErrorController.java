package com.example.csv_parser.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping
    public Map<String, String> handleError(HttpServletRequest request) {
        Object statusObject = request.getAttribute("jakarta.servlet.error.status_code");
        int status = statusObject != null ? (int) statusObject : 500;

        String message = switch (status) {
            case 404 -> "Endpoint not found";
            case 405 -> "Method not allowed";
            case 500 -> "Internal server error";
            default -> "Something went wrong";
        };

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(status));
        errorResponse.put("message", message);
        errorResponse.put("timestamp", LocalDateTime.now().toString());

        return errorResponse;
    }
}
