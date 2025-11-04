package com.example.ContLog;

import com.example.ContLog.entity.User;
import com.example.ContLog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Autoroty {

    @Autowired
    private UserRepository userRepository;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Определяем Имя авторизовавшегося пользователя
    String curUser = authentication.getName();
    // Находим User-а
    User user = userRepository.findByUsername(curUser);

    // Чтение userName User-a
    public String getUserName() {
        return user.getUsername();
    }

    // Чтение Email User-a
    public String getUserEmail() {
        return user.getEmail();
    }

    // Чтение Email User-a
    public int getUserCod() {
        return user.getCod();
    }
}
