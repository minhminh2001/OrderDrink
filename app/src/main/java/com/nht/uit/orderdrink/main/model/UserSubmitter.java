package com.nht.uit.orderdrink.main.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nht.uit.orderdrink.utility.Constain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

/**
 * Created by Win 8.1 Version 2 on 5/4/2017.
 */

public class UserSubmitter {
    private DatabaseReference mData;

    public UserSubmitter(DatabaseReference mData) {
        this.mData = mData;
    }
    public void updateLocation (String idUser, HashMap<String, Object> location){
        mData.child(Constain.USERS).child(idUser).child(Constain.LOCATION).setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("D", "SUCCESS");
                }
                else {
                    Log.d("D", "UNSUCCESS");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("D", e.getMessage());

            }
        });
    }
}
