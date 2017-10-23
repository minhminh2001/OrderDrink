package com.nht.uit.orderdrink.profile_store.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.nht.uit.orderdrink.main.view.MainActivity;
import com.nht.uit.orderdrink.profile_store.model.CreateStoreSubmitter;
import com.nht.uit.orderdrink.profile_store.view.CreateStoreActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Win 8.1 Version 2 on 4/22/2017.
 */

public class CreateStorePresenter {
    private DatabaseReference mData;
    private CreateStoreSubmitter submitter;
    private CreateStoreActivity view;
    private FirebaseAuth mAuth;

    public CreateStorePresenter(CreateStoreActivity view, FirebaseAuth mAuth) {
        this.view = view;
        this.mAuth = mAuth;
        mData = FirebaseDatabase.getInstance().getReference();
        submitter = new CreateStoreSubmitter(mData, view);
    }

    public void createNewStore(String email, String password, final String storeName, final String phoneNumber, final HashMap<String, Object> location, final String from, final String to) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    HashMap<String, Object> favoriteList = new HashMap<>();
                    HashMap<String, Object> products = new HashMap<>();
                    HashMap<String, Object> orderSchedule = new HashMap<>();
                    String timework = from + "-" + to;
                    addNewStore(task.getResult().getUser().getUid().toString(), storeName, task.getResult().getUser().getEmail(), true, 0, phoneNumber, "", timework, location, favoriteList, products, orderSchedule);
                    view.hideProgressDialog();
                    view.showToast("Create new store successful");
                    view.startActivity(new Intent(view, MainActivity.class));
                }
            }
        });
    }

    public void addNewStore(String idStore, String storeName, String email, boolean isStore, int isOpen, String phoneNumber, String linkPhotoStore, String timeWork, HashMap<String, Object>location, HashMap<String, Object> favoriteList, HashMap<String, Object> products, HashMap<String, Object> orderSchedule) {
        submitter.addNewStore(idStore, storeName, email, isStore, isOpen, phoneNumber, linkPhotoStore, timeWork,location, favoriteList, products, orderSchedule);
    }
}
