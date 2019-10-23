package com.example.databindingtest.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.databindingtest.util.TimerWrapper;

/**
 * @author hy
 * @Date 2019/10/23 0023
 **/
public class IntervalTimerViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //父类.class.isAssignableFrom(子类.class)  判断是否为某个类的父类
        if (modelClass.isAssignableFrom(IntervalTimerViewModel.class)){
            return (T) new IntervalTimerViewModel(new TimerWrapper.DefaultTimer());
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
