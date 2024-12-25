package com.parkinglot_backend.dto;

import org.springframework.http.HttpStatus;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-12-24
 * @Description:
 */


public class ApiResponse {
    private String message;
    private HttpStatus status;

    public ApiResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    // Getter and Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
