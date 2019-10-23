package com.example.databindingtest.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.databindingtest.R;
import com.example.databindingtest.data.SimpleViewModel.Popularity;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.databinding.BindingAdapter;

/**
 * @author hy
 * @Date 2019/10/22 0022
 **/
public class BindingAdapters {


    //方法要设置为静态
    //requireAll = true 任何属性发生变化时都会触发
    // 定义progressScale和max两个属性都会触发
    @BindingAdapter(value = {"progressScale", "max"}, requireAll = true)
    public static void setProgress(ProgressBar progressBar, Integer likes, Integer max) {
        if (likes != null && max != null) {
            likes = likes * max / 10;
            if (likes > max) {
                likes = max;
            }
            Log.d("aaa", "setProgress:  progress = " + likes);
            progressBar.setProgress(likes);
        }
    }

    //还可以设置前缀app 也可以设置为android，但是有可能会覆盖原有的属性
    //设置可见性，也可通过表达式直接设置Visibility属性
    @BindingAdapter("app:hideIfZero")
    public static void hideIfZero(View view, Integer number) {
        view.setVisibility(number == 0 ? View.GONE : View.VISIBLE);
    }


    //不加前缀默认为app
    //设置progressBar背景颜色
    @BindingAdapter("tintPopularity")
    public static void tintPopularity(ProgressBar progressBar, Popularity popularity) {
        int color = getAssociatedColor(popularity, progressBar.getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(ColorStateList.valueOf(color));
        }
    }

    @BindingAdapter("popularityIcon")
    public static void popularityIcon(ImageView imageView, Popularity popularity){

        Drawable drawablePopularity = getDrawablePopularity(popularity, imageView.getContext());
        int color = getAssociatedColor(popularity, imageView.getContext());

        ImageViewCompat.setImageTintList(imageView,ColorStateList.valueOf(color));
        imageView.setImageDrawable(drawablePopularity);
    }

    private static int getAssociatedColor(Popularity popularity, Context context) {
        switch (popularity) {
            case NORMAL:
                return ContextCompat.getColor(context, R.color.normal);
            case POPULAR:
                return ContextCompat.getColor(context, R.color.popular);
            case STAR:
                return ContextCompat.getColor(context, R.color.star);
            default:
                return ContextCompat.getColor(context, R.color.normal);
        }
    }

    private static Drawable getDrawablePopularity(Popularity popularity, Context context) {
        switch (popularity) {
            case NORMAL:
                return ContextCompat.getDrawable(context, R.drawable.ic_person_black_96dp);
            case POPULAR:
                return ContextCompat.getDrawable(context, R.drawable.ic_whatshot_black_96dp);
            case STAR:
                return ContextCompat.getDrawable(context, R.drawable.ic_whatshot_black_96dp);
            default:
                return ContextCompat.getDrawable(context, R.drawable.ic_person_black_96dp);
        }
    }
}
