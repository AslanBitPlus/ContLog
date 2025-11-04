package com.example.ContLog.controller;

import com.example.ContLog.entity.Role;
import com.example.ContLog.entity.User;
import com.example.ContLog.repository.RoleRepository;
import com.example.ContLog.repository.UserRepository;
import com.example.ContLog.service.CarrierService;
import com.example.ContLog.service.ContownerService;
import com.example.ContLog.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashSet;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Collections.singletonList(roleRepository.findByName("ROLE_USER")));

        return "register";
    }

    @PostMapping("/register/save")
    public String registerUser(@ModelAttribute User user,
                               Model model) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("errorMessage", "Пользователь под имененм " +
                    user.getUsername() + " зарегистрирован в системе!");
            return "register";
        }


        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.
                findByName("ROLE_USER"))));


        userRepository.save(user);

//        return "redirect:/login";
        return "redirect:/register?success";
    }
}
