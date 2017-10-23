package com.nht.uit.orderdrink.store_list.presenter;

import com.nht.uit.orderdrink.store_list.model.StoreListSubmitter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Win 8.1 Version 2 on 5/4/2017.
 */

public class StoreListPresenter {
    private DatabaseReference mData;
    private StoreListSubmitter submitter;

    public StoreListPresenter() {
        mData = FirebaseDatabase.getInstance().getReference();
        submitter = new StoreListSubmitter(mData);
    }
    public void removeHeart (String idStore, String idUser){
        submitter.removeHeart(idStore, idUser);
    }
    public void addHeart (String idStore, String idUser, String emailUser){
        submitter.addHeart(idStore, idUser, emailUser);
    }

}
