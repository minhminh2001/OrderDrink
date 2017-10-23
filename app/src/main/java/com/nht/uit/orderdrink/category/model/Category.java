package com.nht.uit.orderdrink.category.model;

import com.nht.uit.orderdrink.utility.Constain;

import java.util.HashMap;

/**
 * Created by Win 8.1 Version 2 on 4/25/2017.
 */

public class Category {
    private String idCategory;
    private String categoryName;
    private int sumProduct;
    private String timeCreateCategory;

    public Category() {
    }

    public Category(String idCategory, String categoryName, int sumProduct, String timeCreateCategory) {
        this.idCategory = idCategory;
        this.categoryName = categoryName;
        this.sumProduct = sumProduct;
        this.timeCreateCategory = timeCreateCategory;
    }

    public HashMap<String, Object> putMap (){
        HashMap<String, Object> myMap = new HashMap<>();
        myMap.put(Constain.ID_CATEGORY, idCategory);
        myMap.put(Constain.CATEGORY_NAME, categoryName);
        myMap.put(Constain.SUM_PRODUCT, sumProduct);
        myMap.put(Constain.TIME_CREATE_CATEGORY, timeCreateCategory);
        return myMap;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSumProduct() {
        return sumProduct;
    }

    public void setSumProduct(int sumProduct) {
        this.sumProduct = sumProduct;
    }

    public String getTimeCreateCategory() {
        return timeCreateCategory;
    }

    public void setTimeCreateCategory(String timeCreateCategory) {
        this.timeCreateCategory = timeCreateCategory;
    }
}
