package com.ecommerce.ecommerce_web.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;
    private String correo;
    private String contraseña;

   public Usuario(String usuario, String correo, String contraseña) {
    this.usuario = usuario;
    this.correo = correo;
    this.contraseña = contraseña;
   }
}

