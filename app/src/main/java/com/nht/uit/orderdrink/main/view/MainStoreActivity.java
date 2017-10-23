package com.nht.uit.orderdrink.main.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.category.view.CategoryListFragment;
import com.nht.uit.orderdrink.history_ship_store.model.HistoryShipStore;
import com.nht.uit.orderdrink.history_ship_store.view.HistoryShipStoreFragment;
import com.nht.uit.orderdrink.main.presenter.StorePresenter;
import com.nht.uit.orderdrink.oop.BaseActivity;
import com.nht.uit.orderdrink.product.model.Product;
import com.nht.uit.orderdrink.product.view.CreateProductFragment;
import com.nht.uit.orderdrink.product.view.EditProductFragment;
import com.nht.uit.orderdrink.product_list.view.ProductListFragment;
import com.nht.uit.orderdrink.profile_store.model.Store;
import com.nht.uit.orderdrink.profile_store.presenter.UpdateStorePresenter;
import com.nht.uit.orderdrink.profile_store.view.Profile_Store_Fragment;
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

import java.util.HashMap;

public class MainStoreActivity extends BaseActivity implements AHBottomNavigation.OnTabSelectedListener {

    private AHBottomNavigation ahBottomNavigation;
    private ImageView imgPhotoStore;
    private TextView txtStoreName, txtSumShipped;
    private LinearLayout layoutMyCategory, layoutReport, layoutCreateProduct, layoutRateUs, layoutShare;
    private String idStore, addressUser, linkPhotoStore = "";
    private DatabaseReference mData;
    private Button btnLogout;
    private GPSTracker gps;
    private HashMap<String, Object> location;
    private SwitchCompat switchCompatStatus;
    private boolean isOpen = true, flagNotify = false;
    private Bitmap bitmap = null;
    private double lo = 0, la = 0;
    private int tabIndex = 0;
    private CreateProductFragment createProductFragment;
    private UpdateStorePresenter presenter;
    private StorePresenter storePresenter;
    private boolean flag_exit = true;
    //Notification
    private NotificationCompat.Builder notBuilder;
    private static final int MY_NOTIFICATION_ID = 12345;
    private static final int MY_REQUEST_CODE = 100;

    public MainStoreActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main_store);
        addControls();
        innitInfo();
        checkNotify();
        addEvents();
    }

    private void checkNotify() {
        try {
            mData.child(Constain.STORES).child(idStore).child(Constain.HISTORY_SHIP_STORE).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dt : dataSnapshot.getChildren()) {
                            mData.child(Constain.STORES).child(idStore).child(Constain.HISTORY_SHIP_STORE).child(dt.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        try {
                                            for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                                HistoryShipStore historyShipStore = dt.getValue(HistoryShipStore.class);
                                                if (historyShipStore.getStatusOrder() == 0) {
                                                    notifyNewOrder(historyShipStore.getUserName());
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

    private void addEvents() {
        //My Category
        layoutMyCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                flag_exit = false;
                setTitle("Thư mục của tôi");
                CategoryListFragment categoryListFragment = new CategoryListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_id_store, categoryListFragment).commit();
            }
        });
        //Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        //Opent - Close Store
        switchCompatStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchCompatStatus.isChecked() == true) {
                    presenter.updateStatusStore(idStore, 0);
                } else {
                    isOpen = false;
                    presenter.updateStatusStore(idStore, 1);
                }
            }
        });
        //Create Product
        layoutCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                flag_exit = false;
                setTitle("Tạo Sản Phẩm");
                createProductFragment = new CreateProductFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_id_store, createProductFragment).commit();
            }
        });
        //rate us
        layoutRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
            }
        });
        //share app
        layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.nht.uit.orderdrink");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

    }


    private void innitInfo() {
        try {
            //get Info Store
            mData.child(Constain.STORES).child(idStore).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            Store store = dataSnapshot.getValue(Store.class);
                            addressUser = "";
                            if (store.getLocation() != null) {
                                HashMap<String, Object> flag = new HashMap<>();
                                flag = store.getLocation();
                                addressUser = String.valueOf(flag.get(Constain.ADDRESS));
                            }
                            txtStoreName.setText(store.getStoreName().toString());
                            txtSumShipped.setText(String.valueOf(store.getSumShipped()) + " Shipped");
                            linkPhotoStore = store.getLinkPhotoStore().toString();
                            if (store.getIsOpen() == 0){
                                isOpen = true;
                            }
                            else if (store.getIsOpen() == 1){
                                isOpen = false;
                            }
                            switchCompatStatus.setChecked(isOpen);
                            if (!linkPhotoStore.equals("")) {
                                Glide.with(MainStoreActivity.this)
                                        .load(linkPhotoStore)
                                        .fitCenter()
                                        .into(imgPhotoStore);
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

    private void addControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_store);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_store);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Navigation Bottom
        ahBottomNavigation = (AHBottomNavigation) findViewById(R.id.navigation);
        initItemNavigation();
        mData = FirebaseDatabase.getInstance().getReference();
        txtStoreName = (TextView) findViewById(R.id.txtstorename_mainuser);
        txtSumShipped = (TextView) findViewById(R.id.txtSumShipped_mainstore);
        imgPhotoStore = (ImageView) findViewById(R.id.imgPhotoStore_mainstore);
        btnLogout = (Button) findViewById(R.id.btn_logout_store);
        layoutMyCategory = (LinearLayout) findViewById(R.id.navigation_mycategory);
        layoutCreateProduct = (LinearLayout) findViewById(R.id.navigation_createproduct);
        layoutReport = (LinearLayout) findViewById(R.id.navigation_report);
        layoutRateUs = (LinearLayout) findViewById(R.id.navigation_rateus_store);
        layoutShare = (LinearLayout) findViewById(R.id.navigation_share_store);
        switchCompatStatus = (SwitchCompat) findViewById(R.id.switchCompat_Status);
        gps = new GPSTracker(this);
        location = new HashMap<>();
        //Presenter
        Profile_Store_Fragment fragment = new Profile_Store_Fragment();
        presenter = new UpdateStorePresenter(this, fragment);
        storePresenter = new StorePresenter();
        //get IdStore
        Intent intent = getIntent();
        idStore = intent.getStringExtra(Constain.ID_STORE);
         tabIndex = intent.getIntExtra(Constain.TAB_INDEX, 0);
        ahBottomNavigation.setCurrentItem(tabIndex);
        if (tabIndex == 1){
            HistoryShipStoreFragment historyShipStoreFragment = new HistoryShipStoreFragment();
            setTitle("Lịch sử giao hàng");
            getSupportFragmentManager().beginTransaction().replace(R.id.content_id_store, historyShipStoreFragment).commit();
        }
        moveToProductFragment(idStore);
        //Notification
        notBuilder = new NotificationCompat.Builder(this);
        // Thông báo sẽ tự động bị hủy khi người dùng click vào Panel
        notBuilder.setAutoCancel(true);
    }

    private void initItemNavigation() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Trang chủ", R.drawable.ic_store);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Lịch sử", R.drawable.icon_history);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Trang cá nhân", R.drawable.icon_info);
        ahBottomNavigation.addItem(item1);
        ahBottomNavigation.addItem(item2);
        ahBottomNavigation.addItem(item3);
        ahBottomNavigation.setOnTabSelectedListener(this);
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_store);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else{
            if (flag_exit == true ) {
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
            }
            else {
                flag_exit = true;
                moveToProductFragment(idStore);
                ahBottomNavigation.setCurrentItem(0);
            }
        }
    }

    @Override
    public void onTabSelected(int position, boolean wasSelected) {
        if (position == 0) {
            flag_exit = true;
            moveToProductFragment(idStore);
            setTitle("Danh sách sản phẩm");
        } else if (position == 1) {
            flag_exit = false;
            HistoryShipStoreFragment historyShipStoreFragment = new HistoryShipStoreFragment();
            setTitle("Lịch sử giao hàng");
            getSupportFragmentManager().beginTransaction().replace(R.id.content_id_store, historyShipStoreFragment).commit();
        } else if (position == 2) {
            flag_exit = false;
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constain.IS_STORE, true);
            bundle.putString(Constain.ID_STORE, idStore);
            Profile_Store_Fragment profileStoreFragment = new Profile_Store_Fragment();
            profileStoreFragment.setArguments(bundle);
            setTitle("Trang cá nhân");
            getSupportFragmentManager().beginTransaction().replace(R.id.content_id_store, profileStoreFragment).commit();
        }
    }

    private void logOut() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainStoreActivity.this);
        alert.setMessage("Do you want to logout?");
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainStoreActivity.this, MainActivity.class));
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

    public void notifyNewOrder(String userName) {

        notBuilder.setSmallIcon(R.drawable.shipping);
        notBuilder.setTicker("Có thông báo!");

        // Sét đặt thời điểm sự kiện xẩy ra.
        // Các thông báo trên Panel được sắp xếp bởi thời gian này.
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notBuilder.setSound(alarmSound);
        notBuilder.setWhen(System.currentTimeMillis() + 10 * 1000);
        notBuilder.setContentTitle(userName + " đã đặt hàng của bạn");
        notBuilder.setContentText("Click để xem chi tiết!");

        // Tạo một Intent
        Intent intent = new Intent(this, MainStoreActivity.class);
        intent.putExtra(Constain.TAB_INDEX, 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, MY_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationService = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = notBuilder.build();
        notificationService.notify(MY_NOTIFICATION_ID, notification);
    }

    public void moveToProductFragment(String idStore) {
        Bundle bundle = new Bundle();
        bundle.putString(Constain.ID_STORE, idStore);
        bundle.putBoolean(Constain.IS_STORE, true);
        ProductListFragment newFragment = new ProductListFragment();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_id_store, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void moveToEditProductFragment (Product product){
        flag_exit = false;
        Bundle bundle = new Bundle();
        bundle.putString(Constain.ID_STORE, idStore);
        bundle.putBoolean(Constain.IS_STORE, true);
        bundle.putSerializable(Constain.PRODUCTS, product);
        EditProductFragment newFragment = new EditProductFragment();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_id_store, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
