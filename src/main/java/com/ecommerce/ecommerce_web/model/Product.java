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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private String image;
    private String imageType;
    private boolean available;
    private int stock;

    @JsonIgnore
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "image_bytes", columnDefinition = "bytea")
    private byte[] imageBytes;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
