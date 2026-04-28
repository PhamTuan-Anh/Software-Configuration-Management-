package com.techstore.controller;

import com.techstore.model.Product;
import com.techstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String showHomePage(Model model) {
        // 1. Lấy toàn bộ linh kiện từ SQL lên
        List<Product> products = productRepository.findAll();
        
        // 2. Gửi nguyên danh sách đó sang file index.html
        model.addAttribute("products", products);
        
        // 3. Mở giao diện
        return "index";
    }
}