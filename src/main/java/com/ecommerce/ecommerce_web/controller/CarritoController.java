package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.model.CarritoItem;
import com.ecommerce.ecommerce_web.model.Producto;
import com.ecommerce.ecommerce_web.model.Usuario;
import com.ecommerce.ecommerce_web.Repository.CarritoRepository;
import com.ecommerce.ecommerce_web.Repository.ProductoRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Agregar producto al carrito
   @PostMapping("/agregar/{id}")
public String agregarAlCarrito(@PathVariable("id") Long productoId,
                               @RequestParam(defaultValue = "1") int cantidad,
                               HttpSession session) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        return "redirect:/login";
    }

    Optional<Producto> productoOpt = productoRepository.findById(productoId);
    if (productoOpt.isPresent()) {
        Producto producto = productoOpt.get();

        // Buscar si ya existe en el carrito
        List<CarritoItem> items = carritoRepository.findByUsuario(usuario);
        Optional<CarritoItem> itemExistente = items.stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst();

        if (itemExistente.isPresent()) {
            CarritoItem item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            carritoRepository.save(item);
        } else {
            CarritoItem nuevoItem = new CarritoItem(usuario, producto, cantidad);
            carritoRepository.save(nuevoItem);
        }
    }

    return "redirect:/carrito/ver";
}

    // Ver carrito
    @GetMapping("/ver")
    public String verCarrito(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        List<CarritoItem> items = carritoRepository.findByUsuario(usuario);
        model.addAttribute("items", items);
        return "carrito"; // carrito.html
    }

    // Eliminar un producto del carrito
    @PostMapping("/eliminar")
    public String eliminarDelCarrito(@RequestParam Long itemId) {
        carritoRepository.deleteById(itemId);
        return "redirect:/carrito/ver";
    }

    // Actualizar cantidad
    @PostMapping("/actualizar")
    public String actualizarCantidad(@RequestParam Long itemId,
                                     @RequestParam int cantidad) {
        Optional<CarritoItem> itemOpt = carritoRepository.findById(itemId);
        itemOpt.ifPresent(item -> {
            item.setCantidad(cantidad);
            carritoRepository.save(item);
        });
        return "redirect:/carrito/ver";
    }

    // Finalizar compra (borrar carrito)
    @PostMapping("/comprar")
    public String finalizarCompra(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            List<CarritoItem> items = carritoRepository.findByUsuario(usuario);
            carritoRepository.deleteAll(items);
        }
        return "redirect:/carrito/ver";
    }
}