package com.techstore.service;

import com.techstore.model.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope // Đánh dấu: Mỗi người dùng 1 giỏ hàng riêng biệt lưu trong Session
public class CartService {
    
    // Dùng HashMap để lưu giỏ hàng. Ký hiệu: Map<Mã sản phẩm, Món hàng>
    private Map<Long, CartItem> cart = new HashMap<>();

    // 1. Thêm sản phẩm vào giỏ
    public void add(CartItem item) {
        // Nếu món hàng đã có trong giỏ -> Chỉ tăng số lượng lên 1
        if (cart.containsKey(item.getProductId())) {
            CartItem existingItem = cart.get(item.getProductId());
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            // Nếu chưa có -> Thêm mới vào giỏ
            cart.put(item.getProductId(), item);
        }
    }

    // 2. Xóa món hàng khỏi giỏ
    public void remove(Long id) {
        cart.remove(id); 
    }

    // 3. Đổi thành getAllItems để khớp với Controller
    public Collection<CartItem> getAllItems() {
        return cart.values();
    }

    // 4. Tính tổng số lượng hàng (để hiện lên cái icon giỏ hàng ở Header)
    public int getCount() {
        int count = 0;
        for (CartItem item : cart.values()) {
            count += item.getQuantity();
        }
        return count;
    }

    // 5. Tính tổng số tiền phải thanh toán (Đã an toàn hóa công thức tính)
    public double getAmount() {
        double total = 0;
        for (CartItem item : cart.values()) {
            // Nhân trực tiếp giá với số lượng để tránh lỗi thiếu biến subTotal
            total += (item.getPrice() * item.getQuantity());
        }
        return total;
    }
    
    // 6. Xóa sạch giỏ hàng (Dùng khi đã thanh toán xong)
    public void clear() {
        cart.clear();
    }

    // --------------------------------------------------------
    // CÁC HÀM MỚI BỔ SUNG ĐỂ XỬ LÝ NÚT CỘNG/TRỪ SỐ LƯỢNG
    // --------------------------------------------------------

    // 7. Tăng số lượng lên 1 (+)
    public void increase(Long id) {
        CartItem item = cart.get(id);
        if (item != null) {
            item.setQuantity(item.getQuantity() + 1);
        }
    }

    // 8. Giảm số lượng đi 1 (-)
    public void decrease(Long id) {
        CartItem item = cart.get(id);
        if (item != null) {
            if (item.getQuantity() > 1) {
                // Nếu số lượng > 1 thì trừ đi 1
                item.setQuantity(item.getQuantity() - 1);
            } else {
                // Nếu đang bằng 1 mà bấm trừ tiếp thì xóa luôn món đồ đó
                cart.remove(id);
            }
        }
    }
}