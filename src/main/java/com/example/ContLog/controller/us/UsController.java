package com.example.ContLog.controller.us;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsController {

    @GetMapping("/user_about")
    public String createUseraboutPage(Model model) {
        return "07_user/about"; // ??
    }

    @GetMapping("/user_service")
    public String createUserservicePage(Model model) {
        return "07_user/service"; // ??
    }

    @GetMapping("/user_contacts")
    public String createUsercontactsPage(Model model) {
        return "07_user/contacts"; // ??
    }
}
