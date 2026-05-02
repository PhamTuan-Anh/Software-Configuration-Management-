package com.techstore.model;

public class CartItem {
    
    private Long productId;
    private String name;
    private double price;
    private int quantity;
    private String imageUrl;

    // Constructor rỗng (Bắt buộc phải có để Spring Boot hoạt động)
    public CartItem() {
    }

    // Constructor có tham số (Khớp với lệnh new CartItem trong Controller của bạn)
    public CartItem(Long productId, String name, double price, int quantity, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // --- CÁC HÀM GETTER / SETTER QUAN TRỌNG NHẤT ---

    // Hàm này dành cho CartService của bạn gọi getProductId()
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    // Hàm này cực kỳ quan trọng để file cart.html gọi được ${item.id} ở nút Xóa
    public Long getId() {
        return productId;
    }

    // Hàm này để gọi ${item.name}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Hàm này để gọi ${item.price}
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Hàm này để gọi ${item.quantity}
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Hàm này để gọi ${item.imageUrl}
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // CHÌA KHÓA GIẢI QUYẾT LỖI 500: Hàm này để HTML gọi được ${item.subTotal}
    public double getSubTotal() {
        return this.price * this.quantity;
    }
}