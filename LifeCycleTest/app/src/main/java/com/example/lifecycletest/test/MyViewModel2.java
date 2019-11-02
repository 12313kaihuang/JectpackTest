package com.example.lifecycletest.test;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

/**
 * @author hy
 * @Date 2019/10/30 0030
 **/
public class MyViewModel2 extends ViewModel {


    private String name;

    public MyViewModel2(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //...
}
