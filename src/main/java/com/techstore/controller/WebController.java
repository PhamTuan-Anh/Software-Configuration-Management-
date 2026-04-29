package com.techstore.controller;

import com.techstore.model.Product;
import com.techstore.repository.ProductRepository;
import com.techstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // <-- ĐÃ THÊM DÒNG NÀY

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService; 

    @GetMapping("/")
    public String showHomePage(Model model) {
        // 1. Lấy toàn bộ linh kiện từ SQL lên
        List<Product> products = productRepository.findAll();
        
        // 2. Gửi nguyên danh sách đó sang file index.html
        model.addAttribute("products", products);
        
        // 3. Lấy tổng số lượng hàng trong giỏ và gửi sang giao diện
        model.addAttribute("cartCount", cartService.getCount());
        
        // 4. Mở giao diện
        return "index";
    }

    // Hàm mở trang chi tiết sản phẩm
    @GetMapping("/product/{id}")
    public String viewProductDetail(@PathVariable("id") Long id, Model model) {
        // 1. Tìm sản phẩm trong CSDL theo ID
        Product product = productRepository.findById(id).orElse(null);
        
        // Nếu gõ bậy ID không có thật -> đá về trang chủ
        if (product == null) {
            return "redirect:/";
        }
        
        // 2. Gửi dữ liệu sản phẩm sang giao diện
        model.addAttribute("product", product);
        
        // 3. Mở file detail.html
        return "detail";
    }
}