package com.nht.uit.orderdrink.product_list.model;

import com.nht.uit.orderdrink.utility.Constain;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

/**
 * Created by Win 8.1 Version 2 on 5/11/2017.
 */

public class OrderProductSubmitter {
    private DatabaseReference mData;
    public OrderProductSubmitter(DatabaseReference mData) {
        this.mData = mData;
    }
    public void orderProduct (String idStore, String idUser, String idCategory, String idProduct,  HashMap<String, Object> myMap){
        mData.child(Constain.STORES).child(idStore).child(idUser).child(idCategory).child(idProduct).setValue(myMap);
    }
}
