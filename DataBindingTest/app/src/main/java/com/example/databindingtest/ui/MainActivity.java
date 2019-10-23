package com.example.databindingtest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.databindingtest.R;
import com.example.databindingtest.data.SimpleViewModel;
import com.example.databindingtest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //双向绑定使用 @ = {xxx}

        SimpleViewModel simpleViewModel = new ViewModelProvider(this).get(SimpleViewModel.class);

        //xxyyBinding 对应 xx_yy.xml
        ActivityMainBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewDataBinding.setLifecycleOwner(this);
        viewDataBinding.setViewmodel(simpleViewModel);

        viewDataBinding.toDemo2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TwoWayDemoActivity.class));
            }
        });
    }
}
