package com.example.kipchu.ecommerceapp;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by KIPCHU on 27/06/2019.
 */

@Entity public class Product {
    @Id long id;

    String category,name,price,strikedPrice,description;

    int image;

    public Product(String category, String name, String price, String description, int image) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public Product() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStrikedPrice() {
        return strikedPrice;
    }

    public void setStrikedPrice(String strikedPrice) {
        this.strikedPrice = strikedPrice;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}

