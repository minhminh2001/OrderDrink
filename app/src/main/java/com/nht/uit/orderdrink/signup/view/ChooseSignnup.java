package com.nht.uit.orderdrink.signup.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.profile_store.view.CreateStoreActivity;

public class ChooseSignnup extends AppCompatActivity {

    private Button btnChooseUser;
    private Button btnChooseShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_signnup);
        btnChooseShop = (Button) findViewById(R.id.btnChooseShop);
        btnChooseUser = (Button) findViewById(R.id.btnChooseUser);
        btnChooseShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseSignnup.this, CreateStoreActivity.class));
            }
        });

        btnChooseUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseSignnup.this, SignupUserActivity.class));
            }
        });
    }
}
