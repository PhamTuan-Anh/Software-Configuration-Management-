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
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository; 

    // 1. Mở trang giỏ hàng
    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getAllItems());
        model.addAttribute("cartCount", cartService.getCount());
        model.addAttribute("totalAmount", cartService.getAmount()); 
        
        return "cart";
    }

    // 2.  Thêm vào giỏ hàng 
    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable("id") Long id, HttpServletRequest request) {
        
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            CartItem item = new CartItem(product.getId(), product.getName(), product.getPrice(), 1, product.getImageUrl());
            cartService.add(item);
        }
        
        // Lấy link trang hiện tại một cách an toàn nhất
        String referer = request.getHeader("Referer");
        
        // Nếu lấy được link thì quay lại đúng link đó, không thì về Trang chủ
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        
        return "redirect:/"; 
    }

    // 3. Tăng số lượng sản phẩm lên 1 (+)
    @GetMapping("/cart/increase/{id}")
    public String increaseQuantity(@PathVariable("id") Long id) {
        cartService.increase(id);
        return "redirect:/cart"; 
    }

    // 4. Giảm số lượng sản phẩm đi 1 (-)
    @GetMapping("/cart/decrease/{id}")
    public String decreaseQuantity(@PathVariable("id") Long id) {
        cartService.decrease(id);
        return "redirect:/cart"; 
    }

    // 5. Xóa hoàn toàn khỏi giỏ hàng
    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable("id") Long id) {
        cartService.remove(id); 
        return "redirect:/cart"; 
    }
    @GetMapping("/api/cart/add/{id}")
    @ResponseBody 
    public String addToCartApi(@PathVariable("id") Long id) {
        Product product = productRepository.findById(id).orElse(null);
        
        if (product != null) {
            CartItem item = new CartItem(product.getId(), product.getName(), product.getPrice(), 1, product.getImageUrl());
            cartService.add(item);
        }
        
        return String.valueOf(cartService.getCount());
    }
}