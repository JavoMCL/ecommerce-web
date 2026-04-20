package com.ecommerce.ecommerce_web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagen;
    private String tipoDeImagen;
    private boolean disponible;
    private int stock;

    @JsonIgnore
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "imagen_bytes", columnDefinition = "bytea")
    private byte[] imagenBytes;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
