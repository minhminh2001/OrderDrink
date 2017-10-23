package com.nht.uit.orderdrink.product_list.presenter;

import com.nht.uit.orderdrink.product_list.model.OrderProductSubmitter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Win 8.1 Version 2 on 5/11/2017.
 */

public class OrderProductPresenter {
    private DatabaseReference mData;
    private OrderProductSubmitter submitter;

    public OrderProductPresenter() {
        mData = FirebaseDatabase.getInstance().getReference();
        submitter = new OrderProductSubmitter(mData);
    }
    public void orderProduct (String idStore, String idUser, String idCategory, String idProduct,  HashMap<String, Object> myMap){
        submitter.orderProduct(idStore, idUser, idCategory, idProduct, myMap);
    }
}
