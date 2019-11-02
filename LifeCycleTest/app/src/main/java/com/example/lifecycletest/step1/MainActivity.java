package com.example.lifecycletest.step1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Chronometer;

import com.example.lifecycletest.R;

/**
 * 倒计时，旋转屏幕后会发生重置
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Chronometer chronometer = findViewById(R.id.chronometer);

        chronometer.start();
    }
}
