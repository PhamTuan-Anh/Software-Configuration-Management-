package com.techstore.controller;

import com.techstore.model.Product;
import com.techstore.repository.ProductRepository;
import com.techstore.service.CartService;
import com.techstore.repository.UserRepository;
import jakarta.servlet.http.HttpSession; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService; 

    @GetMapping("/")
    public String showHomePage(Model model, HttpSession session) { // Thêm HttpSession
        
        // --- BỔ SUNG LẤY THÔNG TIN ĐĂNG NHẬP ---
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        model.addAttribute("username", loggedInUser); // Truyền sang HTML
        // ---------------------------------------

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
    public String viewProductDetail(@PathVariable("id") Long id, Model model, HttpSession session) { // Thêm HttpSession
        
        // --- BỔ SUNG LẤY THÔNG TIN ĐĂNG NHẬP ---
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        model.addAttribute("username", loggedInUser);
        // ---------------------------------------

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
    
    @GetMapping("/shop")
    public String showShopPage(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model, 
            HttpSession session) { // Thêm HttpSession
        
        // --- BỔ SUNG LẤY THÔNG TIN ĐĂNG NHẬP ---
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        model.addAttribute("username", loggedInUser);
        // ---------------------------------------

        List<Product> products;
        
        // Ưu tiên 1: Nếu người dùng có gõ chữ tìm kiếm
        if (keyword != null && !keyword.isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(keyword);
            model.addAttribute("currentCategory", "Kết quả tìm kiếm: " + keyword);
        } 
        // Ưu tiên 2: Nếu người dùng bấm vào danh mục bên trái
        else if (category != null && !category.isEmpty()) {
            products = productRepository.findByCategory(category);
            model.addAttribute("currentCategory", category);
        } 
        // Không tìm cũng không lọc -> Hiện tất cả
        else {
            products = productRepository.findAll();
            model.addAttribute("currentCategory", "Tất cả sản phẩm");
        }
        
        model.addAttribute("products", products);
        model.addAttribute("cartCount", cartService.getCount());
        
        return "shop";
    }
 // ================= TRANG THÔNG TIN CÁ NHÂN =================
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return "redirect:/login"; 
        }

   
        var user = userRepository.findByUsername(loggedInUser).orElse(null);
        model.addAttribute("user", user);
        
        // Truyền dữ liệu để hiện Header
        model.addAttribute("username", loggedInUser);
        model.addAttribute("cartCount", cartService.getCount());
        
        return "profile"; // Sẽ mở file profile.html
    }

    // ================= TRANG LỊCH SỬ MUA HÀNG =================
    @GetMapping("/orders")
    public String viewOrders(HttpSession session, Model model) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", loggedInUser);
        model.addAttribute("cartCount", cartService.getCount());
        
        return "orders"; // Sẽ mở file orders.html
    }
}