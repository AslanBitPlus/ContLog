package com.example.ContLog.controller;

import com.example.ContLog.entity.Role;
import com.example.ContLog.entity.User;
import com.example.ContLog.repository.RoleRepository;
import com.example.ContLog.repository.UserRepository;
import com.example.ContLog.service.CarrierService;
import com.example.ContLog.service.ContownerService;
import com.example.ContLog.service.DriverService;
import com.example.ContLog.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;

@Controller
@PreAuthorize("hasRole('SUPERADMIN')")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CarrierService carrierService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private ContownerService contownerService;

    @Autowired
    private DriverService driverService;

    // Чтение всех User-ов
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/users";
    }

    // Создание нового User-а (Get)
    @GetMapping("admin/users/create")
    public String createUserPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Collections.singletonList(roleRepository.findByName("ROLE_USER")));
        model.addAttribute("isUpdate", false);
        return "admin/userscrud"; // ??
    }

    // Создание нового User-а (Post)
    @PostMapping("admin/users/create")
    public String createUser(@ModelAttribute User user,
                             @RequestParam("role") String roleName,
                             Model model) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("errorMessage", "Пользователь под имененм " +
                    user.getUsername() + " зарегистрирован в системе!");
            return "admin/users";
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.
                findByName(roleName))));

        userRepository.save(user);
        return "redirect:/admin/users";
    }

    // Изменение данных User-а (Get)
    @GetMapping("/admin/users/update/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("carriers", carrierService.getAllCarriers());
        model.addAttribute("terminals", terminalService.getAllTerminals());
        model.addAttribute("contowners", contownerService.getAllContowners());
        model.addAttribute("drivers", driverService.getAllDrivers());

        return "admin/editUser";
    }

    // Изменение данных User-а (Post)
    @PostMapping("/admin/users/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute User updatedUser,
                             @RequestParam("role") String roleName,
                             BindingResult result,
                             Model model) {
        // Находим User-а в БД
        User user = userRepository.findById(id).orElseThrow();
        // Обновляем данные User-а
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setCod(updatedUser.getCod());
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.
                findByName(roleName))));

        if(result.hasErrors()){
            model.addAttribute("errorMessage",
                    "Ошибка ввода данных Пользователя "
                            + user.getUsername());

            return "redirect:/admin/users";
        }

        if (!updatedUser.getPassword().isEmpty()) {
            user.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));

        }

        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    // Чтение всех доступных ролей для User-а
    @PostMapping("/admin/roles")
    public String createRole(@RequestParam("roleName") String roleName) {
        if (roleRepository.findByName(roleName) == null) {
            roleRepository.save(new Role(roleName));
        }
        return "redirect:/admin/users";
    }

    // Чтение профиля Для Admin (Get)
    @GetMapping("/admin/profile")
    public String showProfile(Model model) {
        // Определение авторизованного Пользователя
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // В случае не определения авторизовавшегося
        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getPrincipal() == null) {
            return "redirect:/login";
        }

        // Определяем Имя авторизовавшегося пользователя
        String curUser = auth.getName();
        // Проверка
        System.out.println("Имя пользователя :" + curUser);
        //

        // Определение текущего пользовтеля
        User currentUser = userRepository.findByUsername(curUser);

        if (currentUser == null) {
            // Проверка
            System.out.println("Текущий пользователь с именем " + curUser + " НЕ найден!");
            //
        } else {
            // Проверка
            System.out.println("Текущий пользователь с именем " + curUser + " найден!");
            //
            model.addAttribute("user", currentUser);
        }

        // Проверка
        System.out.println("Переход на страницу профиля [" + curUser + "] ========================>");
        //

        return "admin/profile";
    }

    // Чтение профиля (Post)
    @PostMapping("/admin/profile")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                BindingResult result,
                                Model model) {
        // Получаем текущего пользователя из контекста безопасности
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.findByUsername(username);

        if (currentUser == null) {
            return "redirect:/login";
        }

        // Обновляем данные Пользователя
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setCod(updatedUser.getCod());
        // Проверка
        System.out.println("Сохранение данных текущего пользователя [" + username + "] ========================>");
        //

        if (!updatedUser.getPassword().isEmpty()) {
            currentUser.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
            // Проверка
            System.out.println("Изменен пароль пользователя [" + username + "] ========================>");
            //
        }

        // Сохраняем данные профиля Пользователя
        try {
            userRepository.save(currentUser);
            model.addAttribute("successMessage", "Профиль успешно обновлен");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при обновлении профиля");
        }

        return "redirect:/admin/profile";
    }

    // Чтение профиля Для ВСЕХ (Get)
    @GetMapping("/profile")
    public String showAllProfile(Model model) {
        // Определение авторизованного Пользователя
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // В случае не определения авторизовавшегося
        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getPrincipal() == null) {
            return "redirect:/login";
        }
        // Определяем Имя авторизовавшегося пользователя
        String curUser = auth.getName();
        // Определение текущего пользовтеля
        User currentUser = userRepository.findByUsername(curUser);
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }

        return "profile";
    }

    // Обновление профиля Для ВСЕХ (Post)
    @PostMapping("/profile")
    public String updateAllProfile(@ModelAttribute("user") User updatedUser,
                                BindingResult result,
                                Model model) {
        // Получаем текущего пользователя из контекста безопасности
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.findByUsername(username);

        if (currentUser == null) {
            return "redirect:/login";
        }

        // Обновляем данные Пользователя
        currentUser.setEmail(updatedUser.getEmail());

        if (!updatedUser.getPassword().isEmpty()) {
            currentUser.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
        }

        // Сохраняем данные профиля Пользователя
        try {
            userRepository.save(currentUser);
            model.addAttribute("successMessage", "Профиль успешно обновлен");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при обновлении профиля");
        }

        return "redirect:/profile";
    }



}
