package com.nht.uit.orderdrink.category.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.nht.uit.orderdrink.R;

/**
 * Created by Win 8.1 Version 2 on 5/2/2017.
 */

public class CategoryListViewHolder extends RecyclerView.ViewHolder {

    public TextView txtSTT, txtSTTSwipe, txtCategoryName, txtSumProduct, txtTimeCreate, txtDeleteCategory;
    public ImageView imgDelete;
    public SwipeLayout swipeLayout;
    public CategoryListViewHolder(View itemView) {
        super(itemView);
        txtCategoryName = (TextView) itemView.findViewById(R.id.txtCategoryName_itemCategoryList);
        txtSumProduct = (TextView) itemView.findViewById(R.id.txtSumProduct_itemCategoryList);
        txtTimeCreate = (TextView) itemView.findViewById(R.id.txtTimeCreateCategory);
        txtSTT = (TextView) itemView.findViewById(R.id.txtSTT);
        txtDeleteCategory = (TextView) itemView.findViewById(R.id.txtDeleteCategory);
        txtSTTSwipe = (TextView) itemView.findViewById(R.id.txtSTTswipe);

        swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe_Category);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, itemView.findViewById(R.id.layout_left));
        swipeLayout.setRightSwipeEnabled(false);
        swipeLayout.setLeftSwipeEnabled(true);
    }

}
