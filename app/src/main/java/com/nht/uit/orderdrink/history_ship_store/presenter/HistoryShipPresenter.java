package com.nht.uit.orderdrink.history_ship_store.presenter;

import com.nht.uit.orderdrink.history_ship_store.model.HistoreShipSubmitter;
import com.nht.uit.orderdrink.history_ship_store.model.HistoryShipStore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Win 8.1 Version 2 on 5/14/2017.
 */

public class HistoryShipPresenter {
    private DatabaseReference mData;
    private HistoreShipSubmitter submitter;

    public HistoryShipPresenter() {
        mData = FirebaseDatabase.getInstance().getReference();
        submitter = new HistoreShipSubmitter(mData);
    }
    public void createHistoryShip (String idStore, String idUser, String idHistoryShip, HistoryShipStore historyShipStore){
       submitter.createHistoryShip(idStore, idUser, idHistoryShip, historyShipStore);
    }
    public void updateStatusOrderStore (String idStore, String idUser, String idHistoryShip, int statusOrder){
       submitter.updateStatusOrderStore(idStore, idUser, idHistoryShip, statusOrder);
    }
    public void updateStatusOrderUser (String idStore, String idUser, String idHistory, int statusOrder){
        submitter.updateStatusOrderUser(idStore, idUser, idHistory, statusOrder);
    }
    public void updateSumShippedStore (String idStore, int sumShipped){
        submitter.updateSumShippedStore(idStore, sumShipped);
    }
    public void updateSumShippedUser (String idUser, int sumShipped){
        submitter.updateSumShippedUser(idUser, sumShipped);
    }
}
