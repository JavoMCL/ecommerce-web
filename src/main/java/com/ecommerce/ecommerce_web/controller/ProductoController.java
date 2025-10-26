package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.model.Producto;
import com.ecommerce.ecommerce_web.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // Carpeta donde se guardarán las imágenes (puede ser cualquier ruta externa)
    private final String UPLOAD_DIR = "C:/ecommerce-uploads/";

    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto_form";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute Producto producto,
                        @RequestParam("imagenFile") MultipartFile file,
                        HttpServletRequest request) {

        if (!file.isEmpty()) {
            try {
                // Crear carpeta si no existe
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                // Generar nombre único para evitar colisiones
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

                // Guardar archivo en disco
                File dest = new File(uploadDir, filename);
                file.transferTo(dest);

                // Guardar nombre de archivo en la entidad
                producto.setImagen(filename);

            } catch (IOException e) {
                e.printStackTrace();
                // Aquí podrías agregar un mensaje de error al modelo
            }
        }

        productoRepository.save(producto);
        return "redirect:/productos/lista";
    }

    @GetMapping("/lista")
    public String lista(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "producto_lista";
    }
}