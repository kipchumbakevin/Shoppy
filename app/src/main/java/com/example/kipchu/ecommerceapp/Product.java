package com.example.kipchu.ecommerceapp;

/**
 * Created by KIPCHU on 27/06/2019.
 */

public class Product {

    String category,name,price,strikedPrice,description;

    int image;

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

