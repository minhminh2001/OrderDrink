package com.nht.uit.orderdrink.category.presenter;

import com.nht.uit.orderdrink.category.model.CategorySubmitter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Win 8.1 Version 2 on 4/26/2017.
 */

public class CategoryPresenter {
    private DatabaseReference mData;
    private CategorySubmitter submitter;

    public CategoryPresenter() {
        mData = FirebaseDatabase.getInstance().getReference();
        submitter = new CategorySubmitter(mData);
    }
    public void addCategoryOnData (String idStore, String idCategory, String categoryName, int sumProduct, String timeCreate){
        submitter.addCategoryOnData(idStore, idCategory, categoryName, sumProduct, timeCreate);
    }
    public void deleteCategory (String idStore, String idCategory){
        submitter.deleteCategory(idStore, idCategory);
    }
}
