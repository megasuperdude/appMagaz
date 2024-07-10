package com.example.islamdip;

public class Product {
    public String productId, productPrice, productTitle, productImage, productDescription, productSize, productColor, productMaterial, productCategory, productStatus;

    public Product(String productID, String productPrice, String productTitle, String productImage, String productDescription, String productSize, String productColor, String productMaterial, String productCategory, String productStatus) {
        this.productId = productID;
        this.productPrice = productPrice;
        this.productTitle = productTitle;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.productSize = productSize;
        this.productColor = productColor;
        this.productMaterial = productMaterial;
        this.productCategory = productCategory;
        this.productStatus = productStatus;
    }

    public Product(){

    }
}
