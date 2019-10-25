package com.example.mvvmtest.data.util;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

/**
 * @author hy
 * @Date 2019/10/25 0025
 **/
public class StudentBindingAdapters {

    @BindingAdapter("visibility")
    public static void setVisibility(View view, Integer drawableRes) {
        view.setVisibility(drawableRes == null ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("src")
    public static void setImageResource(ImageView imageView, Integer drawableRes) {
        if (drawableRes == null || drawableRes == 0) {
            return;
        }
        imageView.setImageResource(drawableRes);
    }
}
