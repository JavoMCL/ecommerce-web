package com.ecommerce.ecommerce_web.auth.controller;

import com.ecommerce.ecommerce_web.auth.user.User;
import com.ecommerce.ecommerce_web.auth.user.UserRepository;
import com.ecommerce.ecommerce_web.auth.user.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthViewController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/register")
    public String registerView() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("passwordConfirm") String passwordConfirm
    ) {
        if (!password.equals(passwordConfirm)) {
            return "redirect:/register?error=password";
        }

        final User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
        try {
            userRepository.save(user);
            return "redirect:/login?registered";
        } catch (DataIntegrityViolationException ex) {
            // Usually indicates unique constraint violation (email already exists)
            log.warn("Registration failed - possible duplicate email: {}", email, ex);
            return "redirect:/register?error=exists";
        } catch (Exception ex) {
            log.error("Unexpected error during registration for {}", email, ex);
            return "redirect:/register?error=server";
        }
    }
}

