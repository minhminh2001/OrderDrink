package com.nht.uit.orderdrink.main.view;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.history_order_user.view.HistoryOrderUserFragment;
import com.nht.uit.orderdrink.main.presenter.UserPresenter;
import com.nht.uit.orderdrink.oop.BaseActivity;
import com.nht.uit.orderdrink.product.model.Product;
import com.nht.uit.orderdrink.profile_store.model.Store;
import com.nht.uit.orderdrink.profile_store.view.Profile_Store_Fragment;
import com.nht.uit.orderdrink.profile_user.model.User;
import com.nht.uit.orderdrink.profile_user.view.ProfileUser_Fragment;
import com.nht.uit.orderdrink.search_user.model.SearchStore;
import com.nht.uit.orderdrink.search_user.model.SearchStoreAdapter;
import com.nht.uit.orderdrink.store_list.view.Store_List_Fragment;
import com.nht.uit.orderdrink.utility.Constain;
import com.nht.uit.orderdrink.utility.GPSTracker;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MainUserActivity extends BaseActivity implements View.OnClickListener, Serializable, TextWatcher {

    private ImageView imgAvata, iconSearch;
    private TextView txtUserName, txtSumOrdered;
    private DatabaseReference mData;
    private String idUser, userName, linkPhotoUser, sumOrdered, addressUser = "";
    private Button btnLogout;
    private GPSTracker gps;
    private UserPresenter presenter;
    private HashMap<String, Object> location;
    private ArrayList<Product> arrAllProduct;
    private double lo = 0, la = 0;
    private LinearLayout layoutSearch, layoutHome, layoutMyfavorite, layoutOrderHistory, layoutRate, layoutShare, layoutMyProfile;
    private Store_List_Fragment storeListFragment;
    private AutoCompleteTextView edtSearch;
    private ArrayList<SearchStore> arrSearchStore;
    private int viewResourceId;
    private SearchStoreAdapter adapter;
    private boolean flag_exit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main_user);
        addControls();
        checkGPS();
        initInfo();
        addEvvents();
    }

    private void checkGPS() {
        if (gps.canGetLocation()) {
            gps.getLocation();
            lo = gps.getLongitude();
            la = gps.getLatitude();
        } else {
            gps.showSettingsAlert();
        }
    }

    private void addEvvents() {
        btnLogout.setOnClickListener(this);
        layoutSearch.setOnClickListener(this);
        layoutMyProfile.setOnClickListener(this);
        layoutHome.setOnClickListener(this);
        layoutOrderHistory.setOnClickListener(this);
        layoutRate.setOnClickListener(this);
        layoutShare.setOnClickListener(this);
        edtSearch.addTextChangedListener(this);
        edtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchStore searchStore = arrSearchStore.get(position);
                Intent intent = new Intent(MainUserActivity.this, MainUser2Activity.class);
                intent.putExtra(Constain.ID_STORE, searchStore.getIdStore());
                intent.putExtra(Constain.ID_USER, idUser);
                intent.putExtra(Constain.IS_STORE, false);
                startActivity(intent);
            }
        });
    }

    private void initInfo() {
        Intent intent = getIntent();
        idUser = intent.getStringExtra(Constain.ID_USER);
        try {
            mData.child(Constain.USERS).child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            User user = dataSnapshot.getValue(User.class);
                            userName = user.getUserName();
                            addressUser = "";
                            if (user.getLocation() != null) {
                                HashMap<String, Object> flag = new HashMap<>();
                                flag = user.getLocation();
                                addressUser = String.valueOf(flag.get(Constain.ADDRESS));
                            }
                            location.put(Constain.LO, lo);
                            location.put(Constain.LA, la);
                            location.put(Constain.ADDRESS, addressUser);
                            presenter.updateLocation(idUser, location);
                            linkPhotoUser = user.getLinkPhotoUser();
                            sumOrdered = user.getSumOrdered() + " Ordered";
                            if (!linkPhotoUser.equals("")) {
                                Glide.with(MainUserActivity.this)
                                        .load(linkPhotoUser)
                                        .fitCenter()
                                        .into(imgAvata);
                            }
                            txtUserName.setText(userName);
                            txtSumOrdered.setText(sumOrdered);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        showToast("Lỗi không load được User!");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {

        }

    }

    private void addControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //View
        imgAvata = (ImageView) findViewById(R.id.imgPhotoUser);
        txtUserName = (TextView) findViewById(R.id.txtusername_mainuser);
        txtSumOrdered = (TextView) findViewById(R.id.txtSumOreders_mainuser);
        btnLogout = (Button) findViewById(R.id.btn_logout_user);
        layoutSearch = (LinearLayout) findViewById(R.id.layoutSearch);
        iconSearch = (ImageView) findViewById(R.id.iconSearchStore);
        layoutHome = (LinearLayout) findViewById(R.id.navigation_homeUser);
        layoutMyProfile = (LinearLayout) findViewById(R.id.navigation_myprofile);
        layoutMyfavorite = (LinearLayout) findViewById(R.id.navigation_myfavorite);
        layoutOrderHistory = (LinearLayout) findViewById(R.id.navigation_historyorder);
        layoutRate = (LinearLayout) findViewById(R.id.navigation_rateus_user);
        layoutShare = (LinearLayout) findViewById(R.id.navigation_share_user);
        location = new HashMap<>();
        mData = FirebaseDatabase.getInstance().getReference();
        presenter = new UserPresenter();
        gps = new GPSTracker(this);
        arrAllProduct = new ArrayList<>();
        storeListFragment = new Store_List_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_id_user, storeListFragment).commit();
        //search store
        edtSearch = (AutoCompleteTextView) findViewById(R.id.edtSearch);
        arrSearchStore = new ArrayList<>();
        initInfo();
        getArrSearchStore();
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (flag_exit == true) {
                AlertDialog.Builder aler = new AlertDialog.Builder(this);
                aler.setMessage("Bạn có chắc chắn muốn thoát?");
                aler.setCancelable(false);
                aler.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });
                aler.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                aler.create().show();
            } else {
                flag_exit = true;
                getSupportFragmentManager().beginTransaction().replace(R.id.content_id_user, storeListFragment).commit();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        if (view == R.id.btn_logout_user) {
            logOut();
        }
        if (view == R.id.layoutSearch ) {
            // moveToSearchAcitvity();
            if (adapter == null) {
                viewResourceId = R.layout.item_search_store;
                adapter = new SearchStoreAdapter(this, viewResourceId, arrSearchStore, lo, la);
                edtSearch.setAdapter(adapter);
            }
        }
        if (view == R.id.navigation_homeUser) {
            onBackPressed();
            flag_exit = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_id_user, storeListFragment).commit();
        }
        if (view == R.id.navigation_myprofile) {
            onBackPressed();
            moveToProfileFragment();
        }

        if (view == R.id.navigation_historyorder) {
            onBackPressed();
            moveToHistoryFragment();
        }
        if (view == R.id.navigation_rateus_user){
            rateApp();
        }
        if (view == R.id.navigation_share_user){
            shareApp ();
        }
    }

    private void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.nht.uit.orderdrink");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    private void moveToHistoryFragment() {
        flag_exit = false;
        HistoryOrderUserFragment historyOrderUserFragment = new HistoryOrderUserFragment();
        setTitle("Lịch sử order");
        getSupportFragmentManager().beginTransaction().replace(R.id.content_id_user, historyOrderUserFragment).commit();
    }

    private void moveToProfileFragment() {
        flag_exit = false;
        ProfileUser_Fragment profileUserFragment = new ProfileUser_Fragment();
        setTitle("Trang cá nhân");
        getSupportFragmentManager().beginTransaction().replace(R.id.content_id_user, profileUserFragment).commit();
    }

    private void getArrSearchStore() {
        mData.child(Constain.STORES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrSearchStore.clear();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dt : dataSnapshot.getChildren()) {
                        try {
                            Store store = dt.getValue(Store.class);
                            HashMap<String, Object> location = new HashMap<String, Object>();
                            double lo = 0, la = 0;
                            try {
                                if (store.getLocation() != null) {
                                    location = store.getLocation();
                                    lo = (double) location.get(Constain.LO);
                                    la = (double) location.get(Constain.LA);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            SearchStore searchStore = new SearchStore(store.getLinkPhotoStore(), store.getIdStore(), store.getStoreName(), lo, la);
                            arrSearchStore.add(searchStore);
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

    private void logOut() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainUserActivity.this);
        alert.setMessage("Do you want to logout?");
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainUserActivity.this, MainActivity.class));
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constain.REQUEST_CODE_GPS) {
            gps.getLocation();
            lo = gps.getLongitude();
            la = gps.getLatitude();
            location.put(Constain.LO, lo);
            location.put(Constain.LA, la);
            location.put(Constain.ADDRESS, addressUser);
            presenter.updateLocation(idUser, location);
        }
    }

    public void moveToProfileStoreFragment(String idStore) {
        flag_exit = false;
        Profile_Store_Fragment profileStoreFragment = new Profile_Store_Fragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constain.IS_STORE, false);
        bundle.putString(Constain.ID_STORE, idStore);
        profileStoreFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_id_user, profileStoreFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (adapter == null) {
            viewResourceId = R.layout.item_search_store;
            adapter = new SearchStoreAdapter(this, viewResourceId, arrSearchStore, lo, la);
            edtSearch.setAdapter(adapter);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean MyStartActivity(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    //On click event for rate this app button
    public void rateApp () {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Try Google play
        intent.setData(Uri.parse("market://details?id=com.nht.uit.orderdrink"));
        if (!MyStartActivity(intent)) {
            //Market (Google play) app seems not installed, let's try to open a webbrowser
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.nht.uit.orderdrink"));
            if (!MyStartActivity(intent)) {
                //Well if this also fails, we have run out of options, inform the user.
                showToast("Could not open Android market, please install the market app.");
            }
        }
    }
}
