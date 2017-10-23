package com.nht.uit.orderdrink.history_order_user.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.history_order_user.model.HistoryOrderUser;
import com.nht.uit.orderdrink.history_order_user.model.OrderListUserAdapter;
import com.nht.uit.orderdrink.history_order_user.model.ParentHistoryOrderUser;
import com.nht.uit.orderdrink.history_order_user.model.ParentInfoShopOrder;
import com.nht.uit.orderdrink.history_ship_store.model.ListOrderProduct;
import com.nht.uit.orderdrink.utility.Constain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryOrderUserFragment extends Fragment {

    private DatabaseReference mData;
    private String idUser;
    private OrderListUserAdapter adapter;
    private RecyclerView recyclerViewShipStore;
    private RecyclerView.LayoutManager mManager;


    public HistoryOrderUserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_order_user, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addControl();
    }

    private void addControl() {
        idUser = getActivity().getIntent().getStringExtra(Constain.ID_USER);
        mData = FirebaseDatabase.getInstance().getReference();
        recyclerViewShipStore = (RecyclerView) getActivity().findViewById(R.id.recyclerShipStoreShop);
        recyclerViewShipStore.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderListUserAdapter(getContext(), initData());
        adapter.collapseAllParents();
        recyclerViewShipStore.setAdapter(adapter);
    }

    private List<ParentHistoryOrderUser> initData() {
        List<ParentHistoryOrderUser> parentObjectList = new ArrayList<>();
        getListCategory(parentObjectList);
        return parentObjectList;
    }

    private void getListCategory(final List<ParentHistoryOrderUser> parentObjectList) {
        mData.child(Constain.USERS).child(idUser).child(Constain.HISTORY_ORDER_USER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parentObjectList.clear();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dt : dataSnapshot.getChildren()) {
                        if (dt.getValue() != null) {
                            mData.child(Constain.USERS).child(idUser).child(Constain.HISTORY_ORDER_USER).child(dt.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                            try {
                                                HistoryOrderUser historyShipStore = dt.getValue(HistoryOrderUser.class);
                                                ParentInfoShopOrder parentInfoShopOrder = new ParentInfoShopOrder(historyShipStore.getStoreName(), historyShipStore.getLinkPhotoStore(),
                                                        historyShipStore.getPhoneNumber(), historyShipStore.getAddress(), historyShipStore.getTimeOrder() , historyShipStore.getStatusOrder());
                                                List<ListOrderProduct> listOrderProducts = new ArrayList<ListOrderProduct>();
                                                listOrderProducts.add(new ListOrderProduct(historyShipStore.getArrProduct()));
                                                ParentHistoryOrderUser parentHistoryStore = new ParentHistoryOrderUser(listOrderProducts, parentInfoShopOrder);
                                                parentObjectList.add(parentHistoryStore);
                                                Collections.sort(parentObjectList);
                                                adapter.notifyParentDataSetChanged(true);
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
