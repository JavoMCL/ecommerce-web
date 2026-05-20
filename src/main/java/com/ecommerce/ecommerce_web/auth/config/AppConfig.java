package com.ecommerce.ecommerce_web.auth.config;

import com.ecommerce.ecommerce_web.auth.user.Role;
import com.ecommerce.ecommerce_web.auth.user.User;
import com.ecommerce.ecommerce_web.auth.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository repository;

    @Value("${application.security.admin.name:}")
    private String adminName;

    @Value("${application.security.admin.email:}")
    private String adminEmail;

    @Value("${application.security.admin.password:}")
    private String adminPassword;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            final User user = repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            final boolean isAdmin = user.getRole() == Role.ADMIN
                    || (!adminEmail.isBlank() && user.getEmail().equalsIgnoreCase(adminEmail));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(isAdmin
                            ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                            : List.of(new SimpleGrantedAuthority("ROLE_USER")))
                    .build();
        };
    }

    @Bean
    public CommandLineRunner seedAdminUser(PasswordEncoder passwordEncoder) {
        return args -> {
            if (!adminName.isBlank() && !adminEmail.isBlank() && !adminPassword.isBlank() && repository.findByEmail(adminEmail).isEmpty()) {
                repository.save(User.builder()
                        .name(adminName)
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .role(Role.ADMIN)
                        .build());
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
