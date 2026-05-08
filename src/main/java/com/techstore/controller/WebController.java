package com.techstore.controller;

import com.techstore.model.Product;
import com.techstore.model.User;
import com.techstore.repository.ProductRepository;
import com.techstore.repository.UserRepository;
import com.techstore.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import java.util.*;

@Controller
public class WebController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    // API AI
    private final String GEMINI_API_KEY = "AIzaSyCrdjVkvub9a2gaWkqih4llsj2CYgHBTmM";
    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + GEMINI_API_KEY;

    // 1. TRANG CHỦ
    @GetMapping("/")
    public String showHomePage(Model model, HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        model.addAttribute("username", loggedInUser);
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("cartCount", cartService.getCount());
        return "index";
    }

    // 2. TRANG CHI TIẾT SẢN PHẨM
    @GetMapping("/product/{id}")
    public String viewProductDetail(@PathVariable("id") Long id, Model model, HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        model.addAttribute("username", loggedInUser);

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/";

        model.addAttribute("product", product);
        model.addAttribute("cartCount", cartService.getCount());
        return "detail";
    }

    // 3. TRANG CỬA HÀNG (SHOP)
    @GetMapping("/shop")
    public String showShopPage(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model,
            HttpSession session) {
        
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        model.addAttribute("username", loggedInUser);

        List<Product> products;
        if (keyword != null && !keyword.isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(keyword);
            model.addAttribute("currentCategory", "Tìm kiếm: " + keyword);
        } else if (category != null && !category.isEmpty()) {
            products = productRepository.findByCategory(category);
            model.addAttribute("currentCategory", category);
        } else {
            products = productRepository.findAll();
            model.addAttribute("currentCategory", "Tất cả sản phẩm");
        }

        model.addAttribute("products", products);
        model.addAttribute("cartCount", cartService.getCount());
        return "shop";
    }

    // 4. TRANG THÔNG TIN CÁ NHÂN
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        User user = userRepository.findByUsername(loggedInUser).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("username", loggedInUser);
        model.addAttribute("cartCount", cartService.getCount());
        return "profile";
    }

    // 5. TRANG LỊCH SỬ ĐƠN HÀNG
    @GetMapping("/orders")
    public String viewOrders(HttpSession session, Model model) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        model.addAttribute("username", loggedInUser);
        model.addAttribute("cartCount", cartService.getCount());
        return "orders";
    }

    // 6. API CHAT AI 
    @GetMapping("/api/ai/chat")
    @ResponseBody
    public String aiChat(@RequestParam String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String systemPrompt = "Bạn là chuyên gia tư vấn máy tính TechStore AI. "
                + "Nhiệm vụ: Tư vấn cấu hình PC và chẩn đoán lỗi phần cứng. "
                + "Trả lời ngắn gọn, nhiệt tình. Câu hỏi: " + message;

            // Đóng gói JSON gửi Google
            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", systemPrompt);

            Map<String, Object> parts = new HashMap<>();
            parts.put("parts", Collections.singletonList(textPart));

            Map<String, Object> body = new HashMap<>();
            body.put("contents", Collections.singletonList(parts));

            // Gửi Request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(GEMINI_URL, entity, Map.class);

            // Bóc tách JSON lấy câu trả lời
            List candidates = (List) response.getBody().get("candidates");
            Map candidate = (Map) candidates.get(0);
            Map content = (Map) candidate.get("content");
            List resParts = (List) content.get("parts");
            Map resPart = (Map) resParts.get(0);

            return (String) resPart.get("text");

        } catch (Exception e) {
            return "🤖 AI đang bận nâng cấp linh kiện, bạn hỏi lại sau nhé! (Lỗi: Key chưa đúng hoặc hết hạn)";
        }
    }
}