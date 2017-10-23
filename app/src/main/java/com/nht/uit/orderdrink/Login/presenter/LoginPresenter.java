package com.nht.uit.orderdrink.Login.presenter;

import com.nht.uit.orderdrink.Login.model.LoginSubmitter;
import com.nht.uit.orderdrink.Login.view.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Win 8.1 Version 2 on 4/20/2017.
 */

public class LoginPresenter {
    private DatabaseReference mData;
    private LoginSubmitter submitter;
    private LoginActivity view;

    public LoginPresenter(LoginActivity view) {
        this.view = view;
        mData = FirebaseDatabase.getInstance().getReference();
        submitter = new LoginSubmitter(mData, view);
    }
    public void login (String email, String password){
        submitter.login(email, password);
    }
    
}
