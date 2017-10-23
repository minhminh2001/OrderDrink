package com.nht.uit.orderdrink.main.view;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.history_order_user.view.HistoryOrderUserFragment;
import com.nht.uit.orderdrink.main.presenter.UserPresenter;
import com.nht.uit.orderdrink.my_cart.view.DisplayProduct;
import com.nht.uit.orderdrink.my_cart.view.MyCartFragment;
import com.nht.uit.orderdrink.oop.BaseActivity;
import com.nht.uit.orderdrink.product.model.Product;
import com.nht.uit.orderdrink.product_list.view.ProductListFragment;
import com.nht.uit.orderdrink.profile_user.model.User;
import com.nht.uit.orderdrink.profile_user.view.ProfileUser_Fragment;
import com.nht.uit.orderdrink.search_user.model.SearchProduct;
import com.nht.uit.orderdrink.search_user.model.SearchProductAdapter;
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

public class MainUser2Activity extends BaseActivity implements View.OnClickListener, Serializable, AHBottomNavigation.OnTabSelectedListener, TextWatcher {

    private ImageView imgAvata, imgSearch;
    private AHBottomNavigation ahBottomNavigation;
    private TextView txtUserName, txtSumOrdered;
    private DatabaseReference mData;
    private String idUser, idStore, userName, linkPhotoUser, sumOrdered, addressUser = "";
    private Button btnLogout;
    private GPSTracker gps;
    private UserPresenter presenter;
    private HashMap<String, Object> location;
    private ArrayList<Product> arrAllProduct;
    private double lo = 0, la = 0;
    private LinearLayout layoutSearch, layoutHome, layoutMyfavorite, layoutOrderHistory, layoutRate, layoutShare, layoutMyProfile;
    private MyCartFragment myCartFragment;
    private ProductListFragment fragment;
    private AutoCompleteTextView edtSearch;
    private ArrayList<SearchProduct> arrSearchProduct;
    private int viewResourceId;
    private SearchProductAdapter adapter;
    private boolean flag_exit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main_user2);
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
                SearchProduct searchProduct = arrSearchProduct.get(position);
                Product product = searchProduct.getProduct();
                Intent intent = new Intent(MainUser2Activity.this, DisplayProduct.class);
                intent.putExtra(Constain.PRODUCTS, product);
                intent.putExtra(Constain.ID_STORE, idStore);
                startActivity(intent);
            }
        });
    }

    private void initInfo() {
        try {
            //get Info User
            mData.child(Constain.USERS).child(idUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            User user = dataSnapshot.getValue(User.class);
                            userName = user.getUserName();
                            linkPhotoUser = user.getLinkPhotoUser();
                            sumOrdered = user.getSumOrdered() + " Ordered";
                            if (!linkPhotoUser.equals("")) {
                                Glide.with(MainUser2Activity.this)
                                        .load(linkPhotoUser)
                                        .fitCenter()
                                        .into(imgAvata);
                            }
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
            //get Adress
            mData.child(Constain.USERS).child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            User user = dataSnapshot.getValue(User.class);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        Intent intent = getIntent();
        idUser = intent.getStringExtra(Constain.ID_USER);
        idStore = intent.getStringExtra(Constain.ID_STORE);
        myCartFragment = new MyCartFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constain.ID_STORE, idStore);
        bundle.putBoolean(Constain.IS_STORE, false);
        fragment = new ProductListFragment();
        fragment.setArguments(bundle);
        //Navigation Bottom
        ahBottomNavigation = (AHBottomNavigation) findViewById(R.id.navigation_User);
        initItemNavigation();

        //View
        imgAvata = (ImageView) findViewById(R.id.imgPhotoUser2);
        imgSearch = (ImageView) findViewById(R.id.iconSearchStore2);
        txtUserName = (TextView) findViewById(R.id.txtusername_mainuser2);
        txtSumOrdered = (TextView) findViewById(R.id.txtSumOreders_mainuser2);
        btnLogout = (Button) findViewById(R.id.btn_logout_user2);
        layoutSearch = (LinearLayout) findViewById(R.id.layoutSearch2);
        layoutHome = (LinearLayout) findViewById(R.id.navigation_homeUser2);
        layoutMyProfile = (LinearLayout) findViewById(R.id.navigation_myprofile2);
        layoutMyfavorite = (LinearLayout) findViewById(R.id.navigation_myfavorite2);
        layoutOrderHistory = (LinearLayout) findViewById(R.id.navigation_historyorder2);
        layoutRate = (LinearLayout) findViewById(R.id.navigation_rateus_user2);
        layoutShare = (LinearLayout) findViewById(R.id.navigation_share_user2);
        location = new HashMap<>();
        mData = FirebaseDatabase.getInstance().getReference();
        presenter = new UserPresenter();
        gps = new GPSTracker(this);
        arrAllProduct = new ArrayList<>();
        //search Product
        edtSearch = (AutoCompleteTextView) findViewById(R.id.edtSearch2);
        arrSearchProduct = new ArrayList<>();
        viewResourceId = R.layout.item_search_product;
        getArrSearchProduct();
    }

    private void getArrSearchProduct() {
        try {
            mData.child(Constain.STORES).child(idStore).child(Constain.CATEGORY).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dt : dataSnapshot.getChildren()) {
                            mData.child(Constain.STORES).child(idStore).child(Constain.CATEGORY).child(dt.getKey()).child(Constain.PRODUCTS).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                            try {
                                                Product product = dt.getValue(Product.class);
                                                SearchProduct searchProduct = new SearchProduct(product);
                                                arrSearchProduct.add(searchProduct);
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

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (flag_exit == true) {
                moveToStoreList();
            } else {
                flag_exit = true;
                createProductListFragment();
                ahBottomNavigation.setCurrentItem(0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        if (view == R.id.btn_logout_user2) {
            logOut();
        }
        if (view == R.id.layoutSearch2 || view == R.id.iconSearchStore2) {
            if (adapter == null) {
                adapter = new SearchProductAdapter(MainUser2Activity.this, viewResourceId, arrSearchProduct);
                edtSearch.setAdapter(adapter);
            }
        }
        if (view == R.id.navigation_homeUser2) {
            onBackPressed();
            finish();
            moveToStoreList();
        }
        if (view == R.id.navigation_myprofile2) {
            onBackPressed();
            ahBottomNavigation.setCurrentItem(2);
            moveToProfileFragment();

        }
        if (view == R.id.navigation_historyorder2) {
            onBackPressed();
            moveToHistoryFragment();
        }
        if (view == R.id.navigation_rateus_user2){
            rateApp();
        }
        if (view == R.id.navigation_share_user2){
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
        HistoryOrderUserFragment profileUserFragment = new HistoryOrderUserFragment();
        setTitle("Lịch sử order");
        getSupportFragmentManager().beginTransaction().replace(R.id.content_id_user2, profileUserFragment).commit();
    }

    private void moveToStoreList() {
        Intent intent = new Intent(MainUser2Activity.this, MainUserActivity.class);
        intent.putExtra(Constain.ID_USER, idUser);
        intent.putExtra(Constain.IS_STORE, false);
        startActivity(intent);
    }

    private void moveToProfileFragment() {
        flag_exit = false;
        ProfileUser_Fragment profileUserFragment = new ProfileUser_Fragment();
        setTitle("Trang cá nhân");
        getSupportFragmentManager().beginTransaction().replace(R.id.content_id_user2, profileUserFragment).commit();
    }

    private void initItemNavigation() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Sản phẩm", R.drawable.icon_coffe);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Giỏ hàng", R.drawable.icon_shopping);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Trang cá nhân", R.drawable.icon_info);
        ahBottomNavigation.addItem(item1);
        ahBottomNavigation.addItem(item2);
        ahBottomNavigation.addItem(item3);
        ahBottomNavigation.setOnTabSelectedListener(this);
        ahBottomNavigation.setCurrentItem(0);
    }

    private void logOut() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainUser2Activity.this);
        alert.setMessage("Do you want to logout?");
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainUser2Activity.this, MainActivity.class));
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

    @Override
    public void onTabSelected(int position, boolean wasSelected) {
        if (position == 0) {
            createProductListFragment();
        } else if (position == 1) {
            flag_exit = false;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_id_user2, myCartFragment).commit();

        } else if (position == 2) {
            setTitle("Trang cá nhân");
            moveToProfileFragment();
        }
    }

    public void createProductListFragment() {
        flag_exit = true;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_id_user2, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (adapter == null) {
            adapter = new SearchProductAdapter(MainUser2Activity.this, viewResourceId, arrSearchProduct);
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
