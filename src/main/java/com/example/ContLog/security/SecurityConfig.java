package com.example.ContLog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//@EnableGlobalAuthentication
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;


    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityfilterChain(HttpSecurity http) throws Exception {

        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/", "/register").permitAll()
                                .requestMatchers("/carriers/**",
                                        "/containers/**", "/contowners/**",
                                        "/admin/**").hasRole("SUPERADMIN")
                                .requestMatchers("/error/**", "/profile/**").hasAnyRole("SUPERADMIN", "USER",
                                        "SI_MANAGER", "SO_MANAGER", "CO_MANAGER",
                                        "TR_MANAGER", "TC_MANAGER", "DRIVER")
                                .requestMatchers("/07_user/**").hasRole("USER")
                                .requestMatchers("/01_si_manager/**", "si_**").hasAnyRole("SUPERADMIN", "SI_MANAGER")
                                .requestMatchers("/02_so_manager/**", "so_**").hasAnyRole("SUPERADMIN", "SO_MANAGER")
                                .requestMatchers("/07_tc_manager/**", "tc_**").hasAnyRole("SUPERADMIN", "TC_MANAGER")
                                .requestMatchers("/04_tr_manager/**", "tr_**").hasAnyRole("SUPERADMIN", "TR_MANAGER")
                                .requestMatchers("/03_co_manager/**", "co_**", "/containers/**").hasAnyRole("SUPERADMIN", "CO_MANAGER")
                                .requestMatchers("/drivers/**").hasAnyRole("SUPERADMIN",
                                        "TR_MANAGER", "DRIVER")
                                .requestMatchers("/terminals/**").hasAnyRole("SUPERADMIN",
                                        "TR_MANAGER", "TC_MANAGER", "CO_MANAGER", "DRIVER")
//                                .requestMatchers("/drivers/update/{\\d}").hasRole("ADMIN")
//                                .requestMatchers("/drivers/create/{\\d}").hasRole("ADMIN")
//                                .requestMatchers("/drivers/{\\d}/delete").hasRole("ADMIN")
                                .anyRequest().permitAll()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/main", true)
                                .failureUrl("/login?error=true")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }



}
