package com.nht.uit.orderdrink.history_ship_store.model;

import com.nht.uit.orderdrink.product.model.OrderProduct;

import java.util.ArrayList;

/**
 * Created by Win 8.1 Version 2 on 5/15/2017.
 */

public class ListOrderProduct {
    public ArrayList<OrderProduct> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(ArrayList<OrderProduct> orderProductList) {
        this.orderProductList = orderProductList;
    }

    private ArrayList<OrderProduct> orderProductList;

    public ListOrderProduct(ArrayList<OrderProduct> orderProductList) {
        this.orderProductList = orderProductList;
    }
}
