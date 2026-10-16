package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.repository.CartRepository;
import com.ecommerce.ecommerce_web.repository.ProductRepository;
import com.ecommerce.ecommerce_web.auth.user.User;
import com.ecommerce.ecommerce_web.auth.user.UserRepository;
import com.ecommerce.ecommerce_web.model.CartItem;
import com.ecommerce.ecommerce_web.model.Product;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartController(CartRepository cartRepository,
                          ProductRepository productRepository,
                          UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable("id") Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            List<CartItem> items = cartRepository.findByUser(user);
            Optional<CartItem> existingItem = items.stream()
                    .filter(item -> item.getProduct() != null && item.getProduct().getId().equals(productId))
                    .findFirst();

            if (existingItem.isPresent()) {
                CartItem item = existingItem.get();
                item.setAmount(item.getAmount() + quantity);
                cartRepository.save(item);
            } else {
                CartItem newItem = new CartItem(user, product, quantity);
                cartRepository.save(newItem);
            }
        }

        return "redirect:/cart/view";
    }

    @GetMapping("/view")
    public String viewCart(Authentication authentication, Model model) {
        User user = getAuthenticatedUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }

        List<CartItem> items = cartRepository.findByUser(user);
        model.addAttribute("items", items);
        return "cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long itemId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }

        cartRepository.findById(itemId)
                .filter(item -> item.getUser() != null && item.getUser().getId().equals(user.getId()))
                .ifPresent(cartRepository::delete);

        return "redirect:/cart/view";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long itemId,
                                 @RequestParam int quantity,
                                 Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }

        Optional<CartItem> itemOpt = cartRepository.findById(itemId);
        itemOpt.filter(item -> item.getUser() != null && item.getUser().getId().equals(user.getId()))
                .ifPresent(item -> {
                    item.setAmount(quantity);
                    cartRepository.save(item);
                });

        return "redirect:/cart/view";
    }

    @PostMapping("/checkout")
    public String checkout(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        if (user != null) {
            List<CartItem> items = cartRepository.findByUser(user);
            cartRepository.deleteAll(items);
        }
        return "redirect:/cart/view";
    }
}