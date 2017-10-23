package com.nht.uit.orderdrink.search_user.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.search_user.model.SearchStore;
import com.nht.uit.orderdrink.search_user.model.SearchStoreAdapter;
import com.nht.uit.orderdrink.utility.Constain;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements TextWatcher, Serializable {

    private AutoCompleteTextView edtSearch;
    private ArrayList<SearchStore> arrSearchStore;
    private int viewResourceId;
    private DatabaseReference mData;
    private SearchStoreAdapter adapter;
    private double loUser, laUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_store);
        addControls ();
        addEvents ();
    }
    private void addEvents() {
        edtSearch.addTextChangedListener(this);
    }
    private void addControls() {
        mData = FirebaseDatabase.getInstance().getReference();
        edtSearch = (AutoCompleteTextView) findViewById(R.id.edtSearch);
        arrSearchStore = new ArrayList<>();
        initInfo();
        viewResourceId = R.layout.item_search_product;
        adapter = new SearchStoreAdapter(this, viewResourceId, arrSearchStore, loUser, laUser);
        edtSearch.setAdapter(adapter);
    }
    private void initInfo() {
        arrSearchStore = getIntent().getParcelableArrayListExtra("search");
        loUser = getIntent().getDoubleExtra(Constain.LO, 0);
        laUser = getIntent().getDoubleExtra(Constain.LA, 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
