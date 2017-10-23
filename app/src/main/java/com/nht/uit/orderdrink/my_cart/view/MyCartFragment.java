package com.nht.uit.orderdrink.my_cart.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.history_order_user.model.HistoryOrderUser;
import com.nht.uit.orderdrink.history_order_user.presenter.HistoryOrderPresenter;
import com.nht.uit.orderdrink.history_ship_store.model.HistoryShipStore;
import com.nht.uit.orderdrink.history_ship_store.presenter.HistoryShipPresenter;
import com.nht.uit.orderdrink.my_cart.model.MyCart;
import com.nht.uit.orderdrink.my_cart.model.MyCartAdapter;
import com.nht.uit.orderdrink.oop.BaseFragment;
import com.nht.uit.orderdrink.product.model.OrderProduct;
import com.nht.uit.orderdrink.profile_store.model.Store;
import com.nht.uit.orderdrink.profile_store.presenter.UpdateStorePresenter;
import com.nht.uit.orderdrink.profile_store.view.Profile_Store_Fragment;
import com.nht.uit.orderdrink.profile_user.model.User;
import com.nht.uit.orderdrink.profile_user.presenter.UserProfilePresenter;
import com.nht.uit.orderdrink.utility.Constain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;


public class MyCartFragment extends BaseFragment {

    private ArrayList<MyCart> arrMyCart;
    private String idUser;
    private MyCartAdapter adapter;
    private RecyclerView recyclerMyOrder;
    private DatabaseReference mData;
    private TextView txtSumMoney;
    private Button btnPay;
    private float sumMoney = 0;
    private int sumOrderUser = 0, sumOrderStore = 0;
    private String idHistory, idStore, storeName, userName, phoneNumberUser = "", phoneNumberStore, addressStore, addressUser = "", linkPhotoStore, linkPhotoUser, timeCreate;
    private ArrayList<OrderProduct> arrFlagProduct;
    private HistoryOrderPresenter presenter;
    private HistoryShipPresenter shipPresenter;
    private UserProfilePresenter profilePresenter;
    private UpdateStorePresenter storePresenter;

    public MyCartFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_cart, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addControls();
        initInfo();
        addEvents();
    }

    private void addEvents() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int day = now.get(Calendar.DAY_OF_MONTH);
                int month = now.get(Calendar.MONTH);
                int year = now.get(Calendar.YEAR);
                int hour = now.get(Calendar.HOUR);
                int minute = now.get(Calendar.MINUTE);
                timeCreate = hour + "h:" + minute + "p - " + day + "\\" + month + "\\" + year;
                orderBill();
            }
        });
    }

    private void orderBill() {
        if (sumMoney == 0 ){
            showToast("Giỏ hàng trống!!!");
        }
        else {
        AlertDialog.Builder aler = new AlertDialog.Builder(getContext());
        aler.setMessage("Bạn có chắc chăn muốn đặt hàng hóa đơn này?");
        aler.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createHistoryOrder();
            }
        });
        aler.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        aler.show();
        }
    }

    private void createHistoryOrder() {
        //check Id
        try {
            mData.child(Constain.USERS).child(idUser).child(Constain.HISTORY_ORDER_USER).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        boolean flag_id = true;
                        while (flag_id == true) {
                            Random ra = new Random();
                            StringBuilder mBuilder = new StringBuilder();
                            for (int i = 0; i <= 10; i++) {
                                String number = String.valueOf(ra.nextInt(10));
                                mBuilder.append(number);
                            }
                            idHistory = mBuilder.toString();
                            for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                if (idHistory.equals(dt.getKey())) {
                                    flag_id = true;
                                    break;
                                } else {
                                    flag_id = false;
                                }
                            }
                        }
                        boolean flag = true;
                        if (phoneNumberUser.equals("")) {
                            flag = false;
                            showToast("Vui lòng cập nhật số điện thoại trước khi đặt hàng");
                        } else if (addressUser.equals("")) {
                            flag = false;
                            showToast("Vui lòng cập nhật địa trước khi đặt hàng");
                        } else if (phoneNumberUser.equals("") && addressUser.equals("")) {
                            flag = false;
                            showToast("Vui lòng cập nhật số điện thoại và trước khi đặt hàng");
                        }
                        if (flag == true) {
                            HistoryOrderUser historyOrderUser = new HistoryOrderUser(idHistory, idStore, storeName, linkPhotoStore, phoneNumberStore, addressStore, timeCreate, arrFlagProduct, 0);
                            presenter.createHistoryOrder(idUser, idHistory, idStore, historyOrderUser);
                            sumOrderUser += 1;
                            profilePresenter.updateSumOrderUser(idUser, sumOrderUser);
                            //createHistoryShip same ID
                            HistoryShipStore historyShipStore = new HistoryShipStore(idHistory, idUser, userName, linkPhotoUser, phoneNumberUser, addressUser, timeCreate, arrFlagProduct, 0);
                            shipPresenter.createHistoryShip(idStore, idUser, idHistory, historyShipStore);
                            presenter.deleteMyCart(idUser, idStore);
                            arrMyCart.clear();
                            txtSumMoney.setText("0");
                            adapter.notifyDataSetChanged();
                            sumOrderStore += 1;
                            storePresenter.updateSumOrderedStore(idStore, sumOrderStore);
                            showToast("Đặt hàng thành công!, vui lòng đợi điện thoại của cửa hàng trong vòng 5-10p để xác nhận hóa đơn,xin cảm ơn!");
                        }
                    } else {
                        Random ra = new Random();
                        StringBuilder mBuilder = new StringBuilder();
                        for (int i = 0; i <= 10; i++) {
                            String number = String.valueOf(ra.nextInt(10));
                            mBuilder.append(number);
                        }
                        idHistory = mBuilder.toString();
                        boolean flag = true;
                        if (phoneNumberUser.equals("")) {
                            flag = false;
                            showToast("Vui lòng cập nhật số điện thoại trước khi đặt hàng");
                        } else if (addressUser.equals("")) {
                            flag = false;
                            showToast("Vui lòng cập nhật địa trước khi đặt hàng");
                        } else if (phoneNumberUser.equals("") && addressUser.equals("")) {
                            flag = false;
                            showToast("Vui lòng cập nhật số điện thoại và trước khi đặt hàng");
                        }
                        if (flag == true) {
                            HistoryOrderUser historyOrderUser = new HistoryOrderUser(idHistory, idStore, storeName, linkPhotoStore, phoneNumberStore, addressStore, timeCreate, arrFlagProduct, 0);
                            presenter.createHistoryOrder(idUser, idHistory, idStore, historyOrderUser);
                            //createHistoryShip same ID
                            HistoryShipStore historyShipStore = new HistoryShipStore(idHistory, idUser, userName, linkPhotoUser, phoneNumberUser, addressUser, timeCreate, arrFlagProduct, 0);
                            shipPresenter.createHistoryShip(idStore, idUser, idHistory, historyShipStore);
                            presenter.deleteMyCart(idUser, idStore);
                            arrMyCart.clear();
                            txtSumMoney.setText("0");
                            adapter.notifyDataSetChanged();
                            sumOrderStore += 1;
                            storePresenter.updateSumOrderedStore(idStore, sumOrderStore);
                            showToast("Đặt hàng thành công!, vui lòng đợi điện thoại của cửa hàng trong vòng 5-10p để xác nhận hóa đơn,xin cảm ơn!");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {

        }
    }

    private void initInfo() {
        try {
            //Caculator Sum money for bill
            mData.child(Constain.USERS).child(idUser).child(Constain.MY_CART).child(idStore).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    arrMyCart.clear();
                    arrFlagProduct.clear();
                    sumMoney = 0;
                    if (dataSnapshot.getValue() != null) {
                        try {
                            for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                MyCart myCart = dt.getValue(MyCart.class);
                                sumMoney += myCart.getPrice();
                                arrMyCart.add(myCart);
                                adapter.notifyDataSetChanged();
                                arrFlagProduct.add(new OrderProduct(myCart.getProductName(), myCart.getCount(), myCart.getPrice()));
                            }
                            txtSumMoney.setText(Math.round(sumMoney) + " VNĐ");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //get Info Store
            mData.child(Constain.STORES).child(idStore).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            Store store = dataSnapshot.getValue(Store.class);
                            if (store.getStoreName() != null) {
                                storeName = store.getStoreName();
                            }
                            if (store.getPhoneNumber() != null) {
                                phoneNumberStore = store.getPhoneNumber();
                            }
                            if (store.getLinkPhotoStore() != null) {
                                linkPhotoStore = store.getLinkPhotoStore();
                            }
                            if (store.getLocation() != null) {
                                HashMap<String, Object> location = store.getLocation();
                                addressStore = (String) location.get(Constain.ADDRESS);
                            }
                        } catch (Exception ex) {

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //get Info User
            mData.child(Constain.USERS).child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            User user = dataSnapshot.getValue(User.class);
                            if (user.getUserName() != null) {
                                userName = user.getUserName();
                            }
                            if (user.getPhoneNumber() != null) {
                                phoneNumberUser = user.getPhoneNumber();
                            }
                            if (!user.getLinkPhotoUser().equals("")) {
                                linkPhotoUser = user.getLinkPhotoUser();
                            }
                            if (user.getLocation() != null) {
                                HashMap<String, Object> location = user.getLocation();
                                addressUser = (String) location.get(Constain.ADDRESS);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //get SumorderStore
            mData.child(Constain.STORES).child(idStore).child(Constain.HISTORY_SHIP_STORE).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    sumOrderStore = 0;
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dt : dataSnapshot.getChildren()) {
                            mData.child(Constain.STORES).child(idStore).child(Constain.HISTORY_SHIP_STORE).child(dt.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    sumOrderStore += dataSnapshot.getChildrenCount();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    else {
                        sumOrderStore = 0;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //get SumOrderUser
            mData.child(Constain.USERS).child(idUser).child(Constain.HISTORY_ORDER_USER).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    sumOrderUser = 0;
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dt : dataSnapshot.getChildren()) {
                            mData.child(Constain.USERS).child(idUser).child(Constain.HISTORY_ORDER_USER).child(dt.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        sumOrderUser += dataSnapshot.getChildrenCount();
                                    }
                                    catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    else {
                        sumOrderUser = 0;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addControls() {
        arrMyCart = new ArrayList<>();
        idUser = getActivity().getIntent().getStringExtra(Constain.ID_USER);
        mData = FirebaseDatabase.getInstance().getReference();
        adapter = new MyCartAdapter(arrMyCart, getContext(), idUser, idStore);
        recyclerMyOrder = (RecyclerView) getActivity().findViewById(R.id.recyclerViewMyOrder);
        recyclerMyOrder.setAdapter(adapter);
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(getActivity());
        recyclerMyOrder.setLayoutManager(mManager);
        txtSumMoney = (TextView) getActivity().findViewById(R.id.txtSumMoney);
        btnPay = (Button) getActivity().findViewById(R.id.btnPay);
        presenter = new HistoryOrderPresenter();
        shipPresenter = new HistoryShipPresenter();
        Profile_Store_Fragment fragment = new Profile_Store_Fragment();
        storePresenter = new UpdateStorePresenter(getContext(), fragment);
        profilePresenter = new UserProfilePresenter(getContext());
        arrFlagProduct = new ArrayList<>();
        idStore = getActivity().getIntent().getStringExtra(Constain.ID_STORE);
    }
}
