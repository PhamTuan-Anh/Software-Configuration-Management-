package com.techstore.model; 
public class CartItem {
    private Long productId;
    private String name;
    private double price;
    private int quantity;
    private String imageUrl;

    // Constructor
    public CartItem() {
    }

    // Constructor 
    public CartItem(Long productId, String name, double price, int quantity, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // --- GETTER & SETTER ---
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // Tính tổng tiền của món này (Giá x Số lượng)
    public double getSubTotal() {
        return this.price * this.quantity;
    }
}