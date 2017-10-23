package com.nht.uit.orderdrink.product_list.model;

import android.view.View;
import android.widget.TextView;


import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.nht.uit.orderdrink.R;

/**
 * Created by Nhan on 5/9/2017.
 */

public class GroupProductViewHolder extends ParentViewHolder {

    private TextView txtCategoryProduct;
    public GroupProductViewHolder(View itemView) {
        super(itemView);
        txtCategoryProduct = (TextView) itemView.findViewById(R.id.categoryTitle);
    }

    public void setTxtCategoryProductName(String name){
        txtCategoryProduct.setText(name);
    }
}
