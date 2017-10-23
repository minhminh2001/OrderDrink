package com.nht.uit.orderdrink.product_list.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.category.model.Category;
import com.nht.uit.orderdrink.product.model.Product;
import com.nht.uit.orderdrink.product_list.model.AdapterRecycleGallery;
import com.nht.uit.orderdrink.product_list.model.GroupProduct;
import com.nht.uit.orderdrink.product_list.model.ProductListAdapter;
import com.nht.uit.orderdrink.profile_store.model.Store;
import com.nht.uit.orderdrink.utility.Constain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductListFragment extends Fragment {

    private AdapterRecycleGallery adapterAllProduct;
    private ProductListAdapter adapter;
    private ArrayList<Product> arrProduct;
    private RecyclerView recyclerProduct , recyclerAllProduct;
    private DatabaseReference mData;
    private String idStore, phoneNumber = "";
    private Button btnCallNow;
    private boolean isStore;

    private ScrollView scroll;
    private TextView txtStoreName, txtAddressStore, txtTimeWorkStore;
    private ImageView imgCoverStore;

    public ProductListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        isStore = getArguments().getBoolean(Constain.IS_STORE);
        idStore = getArguments().getString(Constain.ID_STORE);
        return inflater.inflate(R.layout.fragment_list_product, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addControls();
        initInfo();
        addEvent ();
    }

    private void addEvent() {
        btnCallNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phoneNumber.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                }
            }
        });
    }

    private void initInfo() {
        try {
            mData.child(Constain.STORES).child(idStore).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Store store = dataSnapshot.getValue(Store.class);
                        if (store.getStoreName() != null) {
                            txtStoreName.setText(store.getStoreName().toString());
                        }
                        if (store.getTimeWork() != null) {
                            txtTimeWorkStore.setText(store.getTimeWork().toString());
                        }
                        if (store.getLocation() != null) {
                            HashMap<String, Object> location = new HashMap<String, Object>();
                            location = store.getLocation();
                            if (location.get(Constain.ADDRESS) != null) {
                                txtAddressStore.setText(location.get(Constain.ADDRESS).toString());
                            }
                        }
                        if (store.getLinkCoverStore() != null){
                            String linkCover = store.getLinkCoverStore();
                            if (!linkCover.equals("")) {
                                Glide.with(getContext())
                                        .load(linkCover)
                                        .fitCenter()
                                        .into(imgCoverStore);
                            }
                        }
                        if (store.getPhoneNumber() != null){
                            phoneNumber = store.getPhoneNumber();
                        }
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
        arrProduct = new ArrayList<>();
        btnCallNow = (Button) getActivity().findViewById(R.id.btnCallshop);
        scroll = (ScrollView) getActivity().findViewById(R.id.scrollbarP);
        imgCoverStore = (ImageView) getActivity().findViewById(R.id.imgCoverStore_productlist);
        mData = FirebaseDatabase.getInstance().getReference();
        recyclerProduct = (RecyclerView) getActivity().findViewById(R.id.recyclerProducts);
        recyclerProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAllProduct = (RecyclerView) getActivity().findViewById(R.id.productSpecial);
        recyclerAllProduct.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false));
        adapter = new ProductListAdapter(idStore, isStore, getContext(), initData());
        adapterAllProduct = new AdapterRecycleGallery(idStore, isStore, getContext() , arrProduct);

        adapter.expandAllParents();
        recyclerAllProduct.setAdapter(adapterAllProduct);
        recyclerProduct.setAdapter(adapter);
        if (isStore == true){
            btnCallNow.setVisibility(View.GONE);
        }
        recyclerProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scroll.scrollTo(dx, dy);
            }
        });
        txtStoreName = (TextView) getActivity().findViewById(R.id.txtStoreName_listproduct);
        txtAddressStore = (TextView) getActivity().findViewById(R.id.txtAddressStore_listproduct);
        txtTimeWorkStore = (TextView) getActivity().findViewById(R.id.txtTimeWork_listproduct);
    }

    private int getTypeForPosition(int position) {
        if (position > 2)
            return 1;
        return 2;
    }

    private List<GroupProduct> initData() {
        List<GroupProduct> parentObjectList = new ArrayList<>();
        getListCategory(parentObjectList);
        return parentObjectList;
    }

    private void getListCategory(final List<GroupProduct> parentObjectList) {

        try {
            mData.child(Constain.STORES).child(idStore).child(Constain.CATEGORY).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    parentObjectList.clear();
                    arrProduct.clear();
                    if (dataSnapshot.getValue() != null) {
                        try {
                            for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                try {
                                    final Category cate = dt.getValue(Category.class);
                                    final List<Product> childList = new ArrayList<>();
                                    //get all product ,then add List item
                                    mData.child(Constain.STORES).child(idStore).child(Constain.CATEGORY).child(dt.getKey()).child(Constain.PRODUCTS).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() != null) {
                                                try {
                                                     for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                                        try {
                                                            Product product = dt.getValue(Product.class);
                                                            childList.add(product);
                                                            arrProduct.add(product);
                                                            adapterAllProduct.notifyDataSetChanged();
                                                        }
                                                        catch (Exception ex){
                                                            ex.printStackTrace();
                                                        }
                                                    }
                                                    GroupProduct category = new GroupProduct(cate.getCategoryName(), childList);
                                                    parentObjectList.add(category);
                                                    adapter.notifyParentDataSetChanged(true);
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
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
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((ProductListAdapter) recyclerProduct.getAdapter()).onSaveInstanceState(outState);
    }

    public ArrayList<Product> getArrProduct() {
        return arrProduct;
    }
}
