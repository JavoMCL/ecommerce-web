package com.ecommerce.ecommerce_web.auth.controller;

import com.ecommerce.ecommerce_web.auth.user.User;
import com.ecommerce.ecommerce_web.auth.user.UserRepository;
import com.ecommerce.ecommerce_web.auth.user.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.regex.Pattern;

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

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        email = email != null ? email.trim() : "";
        boolean exists = userRepository.findByEmail(email).isPresent();
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/register")
    public String register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("passwordConfirm") String passwordConfirm
    ) {
        // Trim whitespace
        name = name != null ? name.trim() : "";
        email = email != null ? email.trim() : "";
        password = password != null ? password.trim() : "";
        passwordConfirm = passwordConfirm != null ? passwordConfirm.trim() : "";

        log.info("Registration attempt for email: {}", email);

        // Validate password length
        if (password.length() < 8) {
            log.warn("Password too short: {} characters", password.length());
            return "redirect:/register?error=length";
        }

        // Validate email format
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            log.warn("Invalid email format: {}", email);
            return "redirect:/register?error=email";
        }

        if (!password.equals(passwordConfirm)) {
            log.warn("Passwords do not match for email: {}", email);
            return "redirect:/register?error=password";
        }

        log.info("All validations passed for email: {}", email);

        final User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
        try {
            userRepository.save(user);
            log.info("User successfully registered: {}", email);
            return "redirect:/login?registered";
        } catch (DataIntegrityViolationException ex) {
            // Usually indicates unique constraint violation (email already exists)
            log.warn("Registration failed - duplicate email: {}", email, ex);
            return "redirect:/register?error=exists";
        } catch (Exception ex) {
            log.error("Unexpected error during registration for {}", email, ex);
            return "redirect:/register?error=server";
        }
    }
}

