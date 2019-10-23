package com.yu.hu.roomtest.activity;

import android.content.Intent;
import android.os.Handler;

import com.yu.hu.roomtest.R;


/**
 * @author hy
 * @Date 2019/10/16 0016
 **/
public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        },1500);

        //doSomething else
    }
}
