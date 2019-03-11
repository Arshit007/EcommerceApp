package com.example.arshit.ecommerceapp.Model;

public class Cart {

   String Price;
   String ProductImage;
   String ProductName;
   String ProductQuantity;


    public Cart() {
    }

    public String getPrice() {
        return Price;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }


    public void setPrice(String price) {
        Price = price;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }
}
