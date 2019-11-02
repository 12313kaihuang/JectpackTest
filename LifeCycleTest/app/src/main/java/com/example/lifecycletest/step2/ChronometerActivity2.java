package com.example.lifecycletest.step2;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.lifecycletest.R;

/**
 * @author hy
 * @Date 2019/10/29 0029
 * <p>
 * 第二步  使用ViewModel存储时间值
 * 重点在{@link Chronometer#setBase(long)}这个方法
 **/
public class ChronometerActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ViewModelStore提供了一个新的ViewModel或以前创建的ViewModel。
        ChronometerViewModel viewModel = ViewModelProviders.of(this).get(ChronometerViewModel.class);

        Chronometer chronometer = findViewById(R.id.chronometer);

        Log.d("ChronometerActivity2", "onCreate: startTime" + viewModel.getStarTime());
        //每次切换横竖屏后都会重新create
        if (viewModel.getStarTime() == null) {
            //elapsedRealtime 返回系统启动到现在的时间
            long time = SystemClock.elapsedRealtime();
            viewModel.setStarTime(time);
            chronometer.setBase(time);
        } else {
            //重点在这里
            chronometer.setBase(viewModel.getStarTime());
        }
        chronometer.start();
    }
}
