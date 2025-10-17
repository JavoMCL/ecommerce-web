package com.ecommerce.ecommerce_web.model;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;
    private String correo;
    private String contraseña;

    public Usuario() {}

    public Usuario(String usuario, String correo, String contraseña) {
        this.usuario = usuario;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    // Getters y setters
    public Long getId() { return id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
}

