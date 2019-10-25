package com.example.mvvmtest.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


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
        outRect.bottom = dip2px(mContext, mBottom);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
