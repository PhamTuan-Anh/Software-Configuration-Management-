package com.techstore.controller;


import com.techstore.model.User;
import com.techstore.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ==================== ĐĂNG NHẬP ====================
    @GetMapping("/login")
    public String showLoginPage(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {
        // Tìm user theo username
        var userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Tên đăng nhập không tồn tại!");
            return "login";
        }

        User user = userOpt.get();

        // So sánh mật khẩu (plain text — cơ bản)
        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Mật khẩu không đúng!");
            return "login";
        }

        // Lưu session
        session.setAttribute("loggedInUser", user.getUsername());
        return "redirect:/dashboard";
    }

    // ==================== ĐĂNG KÝ ====================
    @GetMapping("/register")
    public String showRegisterPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/dashboard";
        }
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam String username,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 Model model) {
        // Kiểm tra mật khẩu khớp
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "register";
        }

        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Tên đăng nhập đã được sử dụng!");
            return "register";
        }

        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Email đã được sử dụng!");
            return "register";
        }

        // Tạo và lưu user mới
        User newUser = new User(username, password, email);
        userRepository.save(newUser);

        model.addAttribute("success", "Đăng ký thành công! Hãy đăng nhập.");
        return "login";
    }

    // ==================== DASHBOARD ====================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", loggedInUser);
        return "dashboard";
    }

    // ==================== ĐĂNG XUẤT ====================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}