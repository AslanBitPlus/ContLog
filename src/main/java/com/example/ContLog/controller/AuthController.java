package com.example.ContLog.controller;

import com.example.ContLog.entity.Carrier;
import com.example.ContLog.entity.User;
import com.example.ContLog.repository.CarrierRepository;
import com.example.ContLog.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Collection;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/main")
    public String mainPage(Model model, HttpSession session) {
        // Определяем данные пользователя вошедшего в Систему


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Определяем Имя авторизовавшегося пользователя
        String curUser = authentication.getName();
        // Находим User-а
        User user = userRepository.findByUsername(curUser);
        // E-mail и cod профиля
        String userEmail = user.getEmail();
        int userCod = user.getCod();

        // Отправляем данные в виде аттрибутов в сессию
        session.setAttribute("userName", curUser);
        session.setAttribute("userEmail", userEmail);
        session.setAttribute("userCod", String.valueOf(userCod));

        model.addAttribute("curUserName", curUser);
        model.addAttribute("curUserEmail", userEmail);
//        model.addAttribute("CurUserCod", userCod);


        // Определение ролей пользователя
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            // Администратор
            boolean isSuperadmin = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_SUPERADMIN"));
            // Пользователь без прав
            boolean isUser = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
            // Менеджер приема заказов на перевозки КТК
            boolean isSI_MANAGER = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_SI_MANAGER"));
            // Менеджер распределения заявок на перевозку КТК
            boolean isSO_MANAGER = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_SO_MANAGER"));
            // менеджер Contowner-а
            boolean isCO_MANAGER = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_CO_MANAGER"));
            // менеджер Терминала
            boolean isTR_MANAGER = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_TR_MANAGER"));
            // менеджер Транспортнгой компании
            boolean isTC_MANAGER = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_TC_MANAGER"));
            // Водитель
            boolean isDRIVER = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_DRIVER"));


            if (isSuperadmin) {
                // ROLE_SUPERADMIN
                model.addAttribute("menu", 1);
                model.addAttribute("curUserrole", "Администратор Системы");
            } else if (isUser) {
                // ROLE_USER
                model.addAttribute("menu", 2);
                model.addAttribute("curUserrole", "Пользователь без прав!");
            } else if (isSI_MANAGER) {
                // ROLE_SI_MANAGER
                model.addAttribute("menu", 3);
                model.addAttribute("curUserrole", "Менеджер по приему заявок");
            }else if (isSO_MANAGER) {
                // ROLE_SO_MANAGER
                model.addAttribute("menu", 4);
                model.addAttribute("curUserrole", "Менеджер по распределению заявок");
            }else if (isCO_MANAGER) {
                // ROLE_CO_MANAGER
                model.addAttribute("menu", 5);
                model.addAttribute("curUserrole", "Менеджер Владельца КТК");
            }else if (isTR_MANAGER) {
                // ROLE_TR_MANAGER
                model.addAttribute("menu", 6);
                model.addAttribute("curUserrole", "Менеджер терминала");
            }else if (isTC_MANAGER) {
                // ROLE_TC_MANAGER
                model.addAttribute("menu", 7);
                model.addAttribute("curUserrole", "Менеджер Транспортной компании");
            }else if (isDRIVER) {
                // ROLE_DRIVER
                model.addAttribute("menu", 8);
                model.addAttribute("curUserrole", "Водитель");
            }

        }

        // Проверка Аутентификации
        System.out.println("Аутентификация =====================================");
        System.out.println("Пользователь прошел Аутентификацию");
        System.out.println("Authentication object: " +
                SecurityContextHolder.getContext().getAuthentication());
        System.out.println("====================================================");
        System.out.println("Имя пользователя   :" + curUser);
        System.out.println("Email              :" + userEmail);
        System.out.println("Код                :" + userCod);
        System.out.println("====================================================");
        //

        return "main";
    }

}
