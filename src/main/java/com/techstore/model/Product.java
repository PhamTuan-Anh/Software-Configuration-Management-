package com.techstore.model;

import jakarta.persistence.*;

@Entity
public class Product {
    
    @Id
    private Long id; 
    
    private String name;
    private String category;
    private Double price;
    private String specs;
    
    @Column(length = 1000) 
    private String imageUrl;
    
    @Column(columnDefinition = "TEXT") 
    private String description;

    // Constructors
    public Product() {}

    public Product(Long id, String name, String category, Double price, String specs, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.specs = specs;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Getters and Setters 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getSpecs() { return specs; }
    public void setSpecs(String specs) { this.specs = specs; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}