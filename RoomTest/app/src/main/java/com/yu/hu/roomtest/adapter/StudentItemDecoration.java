package com.yu.hu.roomtest.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.hu.roomtest.util.Utils;

/**
 * @author hy
 * @Date 2019/10/14 0014
 **/
public class StudentItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private int mBottom;

    public StudentItemDecoration(Context context, int dpValue) {
        this.mContext = context;
        this.mBottom = dpValue;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = Utils.dip2px(mContext, mBottom);
    }


}
