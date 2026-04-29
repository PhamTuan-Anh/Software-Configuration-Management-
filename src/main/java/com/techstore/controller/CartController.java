package com.techstore.controller;

import com.techstore.model.CartItem;
import com.techstore.model.Product;
import com.techstore.repository.ProductRepository;
import com.techstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository; 

    // 1. Khi bấm vào biểu tượng Giỏ hàng trên Header -> Mở trang giỏ hàng
    @GetMapping("")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("totalAmount", cartService.getAmount());
        return "cart"; // Lát nữa mình sẽ tạo file cart.html
    }

    // 2. Khi bấm dấu "+" ở từng sản phẩm -> Thêm vào giỏ và load lại trang chủ
    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable("id") Long id) {
        // Tìm sản phẩm trong CSDL theo ID
        Product product = productRepository.findById(id).orElse(null);
        
        if (product != null) {
            // Đóng gói thành CartItem và ném vào Service
            CartItem item = new CartItem(product.getId(), product.getName(), product.getPrice(), 1, product.getImageUrl());
            cartService.add(item);
        }
        
        return "redirect:/";
    }
}