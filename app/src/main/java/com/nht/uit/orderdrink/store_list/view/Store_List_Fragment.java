package com.nht.uit.orderdrink.store_list.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.oop.BaseFragment;
import com.nht.uit.orderdrink.profile_store.model.Store;
import com.nht.uit.orderdrink.store_list.model.StoreListAdapter;
import com.nht.uit.orderdrink.utility.Constain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Store_List_Fragment extends BaseFragment {

    private RecyclerView recyclerView;
    private DatabaseReference mData;
    private ArrayList<Store> arrStore;
    private StoreListAdapter adapter;
    private LinearLayoutManager mManager;
    private String idUser, emailUser;

    public Store_List_Fragment() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idUser = getIdUser();
        emailUser = getEmailUser();
        addControls ();
    }

    private void addControls() {
        mData = FirebaseDatabase.getInstance().getReference();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store__list_, container, false);
        arrStore = new ArrayList<>();
        adapter = new StoreListAdapter(arrStore, this, idUser, getContext());
        adapter.setEmailUser(emailUser);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewliststore);
        mManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        return view;
    }

    public ArrayList<Store> getArrStore() {
        return arrStore;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            mData.child(Constain.STORES).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            arrStore.clear();
                            for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                Store store = dt.getValue(Store.class);
                                arrStore.add(store);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            recyclerView.setLayoutManager(mManager);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
