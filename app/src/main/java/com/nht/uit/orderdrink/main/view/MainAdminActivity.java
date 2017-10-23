package com.nht.uit.orderdrink.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.oop.BaseActivity;
import com.nht.uit.orderdrink.profile_store.view.CreateStoreActivity;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class MainAdminActivity extends BaseActivity {

     private Button btnCreateStore, btnUserList, btnStoreList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main_admin);
        addControls ();
        addEvents ();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout){
            LoginManager.getInstance().logOut();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainAdminActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {
        btnCreateStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAdminActivity.this, CreateStoreActivity.class));
            }
        });
    }

    private void addControls() {
        btnCreateStore = (Button) findViewById(R.id.btncreatestore);
        btnUserList = (Button) findViewById(R.id.btnlistuser);
        btnStoreList = (Button) findViewById(R.id.btnliststore);
    }
}
