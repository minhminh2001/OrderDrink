package com.nht.uit.orderdrink.signup.model;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.nht.uit.orderdrink.main.view.MainActivity;
import com.nht.uit.orderdrink.profile_user.model.User;
import com.nht.uit.orderdrink.signup.view.SignupUserActivity;
import com.nht.uit.orderdrink.utility.Constain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

/**
 * Created by Win 8.1 Version 2 on 4/20/2017.
 */

public class SignUpActvitySubmitter {
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private SignupUserActivity view;

    public SignUpActvitySubmitter(DatabaseReference mData, SignupUserActivity view) {
        this.mData = mData;
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
    }
    public void signUpWithEmail (String password, final String userName, final String email, final String phoneNumber, final String linkPhotoUser, final String birthDay, final boolean isStore, final HashMap<String, Object> location, final HashMap<String, Object> favorite_drink){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    view.hideProgressDialog();
                    view.showToast("Đăng ký thành công");
                    addUser(task.getResult().getUser().getUid(), userName, task.getResult().getUser().getEmail(), true,  phoneNumber, linkPhotoUser, birthDay, isStore, 0, location, favorite_drink);
                    view.startActivity(new Intent(view, MainActivity.class));
                }
                else {
                    view.hideProgressDialog();
                    view.showToast("Email đã được đăng ký, vui lòng thử lại!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.hideProgressDialog();
                view.showToast("Đăng ký không thành công!");
            }
        });
    }
    //create new user on firebase
    public void addUser (String idUser,  String userName, String email, boolean gender, String phoneNumber, String linkPhotoUser, String birthDay, boolean isStore, int sumOrdered, HashMap<String, Object>location, HashMap<String, Object> favorite_drink){
        User user = new User(idUser, userName, email, gender, phoneNumber, linkPhotoUser, birthDay, false, sumOrdered, 0, location, favorite_drink);
        HashMap<String, Object> myMap = new HashMap<>();
        myMap = user.putMap();
        mData.child(Constain.USERS).child(idUser).setValue(myMap);
    }
}
