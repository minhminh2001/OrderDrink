package com.nht.uit.orderdrink.Login.model;

import android.support.annotation.NonNull;

import com.nht.uit.orderdrink.Login.view.LoginActivity;
import com.nht.uit.orderdrink.profile_user.model.User;
import com.nht.uit.orderdrink.utility.Constain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

/**
 * Created by Win 8.1 Version 2 on 4/20/2017.
 */

public class LoginSubmitter {

    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private LoginActivity view;

    public LoginSubmitter(DatabaseReference mData, LoginActivity view) {
        this.mData = mData;
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
    }
    public void login (String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    view.hideProgressDialog();
                    view.showToast("Đăng Nhập Thành Công");
                    view.moveToMainActivity();
                }
                else {
                    view.hideProgressDialog();
                    view.showToast("Email hoặc password không đúng");
                }
            }
        });
    }
    //create new user on firebase
    public void addUser (String idUser, String userName, String email, boolean gender, String phoneNumber, String linkPhotoUser, String birthDay, HashMap<String, Object> location, HashMap<String, Object> favorite_drink){
        User user = new User(idUser, userName, email, gender, phoneNumber, linkPhotoUser, birthDay, false, 0, 0, location, favorite_drink);
        HashMap<String, Object> myMap = new HashMap<>();
        myMap = user.putMap();
        mData.child(Constain.USERS).child(idUser).setValue(myMap);
    }
}
