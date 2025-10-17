package com.ecommerce.ecommerce_web.Service;
import com.ecommerce.ecommerce_web.Repository.UsuarioRepository;
import com.ecommerce.ecommerce_web.model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean autenticar(String correo, String contraseña) {
        return usuarioRepository.findByCorreo(correo)
                .map(u -> u.getContraseña().equals(contraseña))
                .orElse(false);
    }

    public boolean registrar(String usuario, String correo, String contraseña) {
        // Verificar si ya existe el correo
        if (usuarioRepository.findByCorreo(correo).isPresent()) {
            return false;
        }

        Usuario nuevo = new Usuario(usuario, correo, contraseña);
        usuarioRepository.save(nuevo);
        return true;
    }
}

