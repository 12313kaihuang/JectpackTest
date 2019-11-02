package com.example.lifecycletest.test;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author hy
 * @Date 2019/10/30 0030
 **/
public class MyViewModelFactory implements ViewModelProvider.Factory {

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MyViewModel2.class)) {
            return (T) new MyViewModel2("Tom");
        }
        throw new RuntimeException("unknown class :" + modelClass.getName());
    }

}
