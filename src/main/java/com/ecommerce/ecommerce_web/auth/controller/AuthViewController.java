package com.ecommerce.ecommerce_web.auth.controller;

import com.ecommerce.ecommerce_web.auth.usuario.User;
import com.ecommerce.ecommerce_web.auth.usuario.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequiredArgsConstructor
public class AuthViewController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/registro")
    public String registerView() {
        return "registro";
    }

    @PostMapping("/registro")
    public String register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("passwordConfirm") String passwordConfirm
    ) {
        if (!password.equals(passwordConfirm)) {
            return "redirect:/registro?error=password";
        }

        final User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);
        return "redirect:/login?registered";
    }
}

