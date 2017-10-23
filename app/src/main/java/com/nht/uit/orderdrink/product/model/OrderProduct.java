package com.nht.uit.orderdrink.product.model;

/**
 * Created by Win 8.1 Version 2 on 5/13/2017.
 */

public class OrderProduct {
    private String  productName;
    private int count;
    private float price;

    public OrderProduct() {
    }

    public OrderProduct(String productName, int count, float price) {
        this.productName = productName;
        this.count = count;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
