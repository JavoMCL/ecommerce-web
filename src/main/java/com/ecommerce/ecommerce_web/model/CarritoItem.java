package com.ecommerce.ecommerce_web.model;

import com.ecommerce.ecommerce_web.auth.usuario.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "carrito_items")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User usuario;

    @ManyToOne
    private Producto producto;

    private int cantidad;

    public CarritoItem(User usuario, Producto producto, int cantidad){
        this.usuario = usuario;
        this.producto = producto;
        this.cantidad = cantidad;
    }
}
