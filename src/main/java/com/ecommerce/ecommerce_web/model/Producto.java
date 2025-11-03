package com.ecommerce.ecommerce_web.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagen;
    private Boolean activo = true; 

@ManyToOne
@JoinColumn(name = "categoria_id")
@JsonBackReference
private Categoria categoria;

}
