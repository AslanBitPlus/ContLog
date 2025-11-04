package com.example.ContLog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/403")
    public String accessDenied403() {
        return "error/403"; // Возвращает страницу 403
    }

    @GetMapping("/error/404")
    public String accessDenied404() {
        return "error/404"; // Возвращает страницу 404
    }

    @GetMapping("/error/500")
    public String internalServerError500() {
        return "error/500"; // Возвращает страницу 500
    }
}
