package com.example.arshit.ecommerceapp.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderModel {

    public static class Order {
        private String ProductAddress;
        private String ProductCity;
        private String ProductCountry;
        private String ProductContact;
        private HashMap<String, String> cartId;

        public Order(String productAddress, String productCity, String productCountry, String productContact, HashMap<String, String> cartId) {
            ProductAddress = productAddress;
            ProductCity = productCity;
            ProductCountry = productCountry;
            ProductContact = productContact;
            this.cartId = cartId;
        }

        public String getProductAddress() {
            return ProductAddress;
        }

        public void setProductAddress(String productAddress) {
            ProductAddress = productAddress;
        }

        public String getProductCity() {
            return ProductCity;
        }

        public void setProductCity(String productCity) {
            ProductCity = productCity;
        }

        public String getProductCountry() {
            return ProductCountry;
        }

        public void setProductCountry(String productCountry) {
            ProductCountry = productCountry;
        }

        public String getProductContact() {
            return ProductContact;
        }

        public void setProductContact(String productContact) {
            ProductContact = productContact;
        }

        public HashMap<String, String> getCartId() {
            return cartId;
        }

        public void setCartId(HashMap<String, String> cartId) {
            this.cartId = cartId;
        }

        public void addRemark(String remarkKey, String remark) {

        }

        public Order() {
        }

        public void setCartId(String CartIdName, String CartId) {

            cartId.put(CartIdName, CartId);
        }
    }

}
