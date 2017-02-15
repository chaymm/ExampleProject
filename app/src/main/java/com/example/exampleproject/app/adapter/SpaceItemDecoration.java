package com.example.exampleproject.app.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chang on 2017/2/8.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int left,top,right,bottom;

    public SpaceItemDecoration(int left,int top,int right,int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = left;
        outRect.top = top;
        outRect.right = right;
        outRect.bottom = bottom;

        //            //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
//                    if (parent.getChildLayoutPosition(view) != 0) {
//                        outRect.left = 0;
//                    }
    }
}