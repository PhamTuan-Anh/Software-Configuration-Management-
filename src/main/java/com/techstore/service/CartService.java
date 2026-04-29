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
    public void remove(Long productId) {
        cart.remove(productId);
    }

    // 3. Lấy toàn bộ danh sách hàng trong giỏ ra để hiển thị lên web
    public Collection<CartItem> getCartItems() {
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

    // 5. Tính tổng số tiền phải thanh toán
    public double getAmount() {
        double total = 0;
        for (CartItem item : cart.values()) {
            total += item.getSubTotal();
        }
        return total;
    }
    
    // 6. Xóa sạch giỏ hàng (Dùng khi đã thanh toán xong)
    public void clear() {
        cart.clear();
    }
}