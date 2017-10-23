package com.nht.uit.orderdrink.main.presenter;

import com.nht.uit.orderdrink.main.model.StoreSubmitter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Win 8.1 Version 2 on 5/12/2017.
 */

public class StorePresenter {
    private DatabaseReference mData;
    private StoreSubmitter submitter;

    public StorePresenter() {
        mData = FirebaseDatabase.getInstance().getReference();
        submitter = new StoreSubmitter(mData);
    }
    public void updateLocation (String idUser, HashMap<String, Object> location){
        submitter.updateLocation(idUser, location);
    }
}
