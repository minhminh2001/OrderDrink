package com.nht.uit.orderdrink.product.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.nht.uit.orderdrink.main.view.MainStoreActivity;
import com.nht.uit.orderdrink.product.model.Product;
import com.nht.uit.orderdrink.product.model.ProductSubmitter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nht.uit.orderdrink.utility.Constain;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * Created by Win 8.1 Version 2 on 5/8/2017.
 */

public class ProductPresenter {
    private DatabaseReference mData;
    private ProductSubmitter submitter;
    private StorageReference mStorage;
    private MainStoreActivity view;

    public ProductPresenter(MainStoreActivity view) {
        this.view = view;
        mData = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();;
        submitter = new ProductSubmitter(view, mData, mStorage);
    }
    public void createProduct (Bitmap bitmap, String idStore, String idCategory, String idProduct, String productName, String describeProduct, float price, int sumProduct){
        submitter.createProduct(bitmap, idStore, idCategory, idProduct, productName, describeProduct, price, sumProduct);
    }
    public void deleteProduct (String idStore, String idCategory, String idProduct, int sumProduct){
        submitter.deleteProduct(idStore, idCategory, idProduct, sumProduct);
    }
    public void updateProductNonLink (String idStore, String idCategory, String idProduct, Product product){
       submitter.updateProductNonLink(idStore, idCategory, idProduct, product);
    }
    public void updateProduct (String idStore, String idCategory, String idProduct, Bitmap bitmap, String productName, float price, final String describeProduct){
        submitter.updateProduct(idStore, idCategory, idProduct, bitmap, productName, price, describeProduct);
    }
}
