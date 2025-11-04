package com.example.ContLog;

import com.example.ContLog.entity.Role;
import com.example.ContLog.entity.User;
import com.example.ContLog.repository.RoleRepository;
import com.example.ContLog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Создаем роли
        createRoleIfNotFound("ROLE_SUPERADMIN");
        createRoleIfNotFound("ROLE_USER");
        createRoleIfNotFound("ROLE_SI_MANAGER");
        createRoleIfNotFound("ROLE_SO_MANAGER");
        createRoleIfNotFound("ROLE_CO_MANAGER");
        createRoleIfNotFound("ROLE_TR_MANAGER");
        createRoleIfNotFound("ROLE_TC_MANAGER");
        createRoleIfNotFound("ROLE_DRIVER");

        // Создаем суперпользователя admin (с полными правами)
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
            admin.setEmail("admin@example.com");
            admin.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName("ROLE_SUPERADMIN"))));
            userRepository.save(admin);
        }

        // Создаем пользователя user (без прав)
        if (userRepository.findByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(new BCryptPasswordEncoder().encode("user"));
            user.setEmail("user@example.com");
            user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName("ROLE_USER"))));
            userRepository.save(user);
        }

    }

    private Role createRoleIfNotFound(String name) {
        if (roleRepository.findByName(name) == null) {
            Role role = new Role();
            role.setName(name);
            // return roleRepository.save(new Role(name));
            roleRepository.save(role);
        }
        return roleRepository.findByName(name);
    }


}
