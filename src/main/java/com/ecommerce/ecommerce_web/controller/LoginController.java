package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.model.Usuario;
import com.ecommerce.ecommerce_web.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ---------------- LOGIN ----------------
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // login.html en templates/
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String contraseña,
                                Model model) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findByCorreo(correo);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            if (usuario.getContraseña().equals(contraseña)) {
                model.addAttribute("success", "Inicio de sesión exitoso ✅");
                return "login"; // o redirigir a dashboard
            }
        }
        model.addAttribute("error", "Credenciales incorrectas ❌");
        return "login";
    }

    // ---------------- REGISTRO ----------------
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro"; // registro.html en templates/
    }

    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String usuario,
                                   @RequestParam String correo,
                                   @RequestParam String contraseña,
                                   Model model) {

        Optional<Usuario> existingUser = usuarioRepository.findByCorreo(correo);
        if (existingUser.isPresent()) {
            model.addAttribute("error", "El correo ya está registrado ❌");
            return "registro";
        }

        Usuario nuevoUsuario = new Usuario(usuario, correo, contraseña);
        usuarioRepository.save(nuevoUsuario);
        model.addAttribute("success", "Usuario registrado con éxito 🎉");
        return "login";
    }
}

