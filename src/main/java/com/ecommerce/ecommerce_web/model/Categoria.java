package com.ecommerce.ecommerce_web.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
@JsonManagedReference
private List<Producto> productos;

}
