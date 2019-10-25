package com.example.mvvmtest.data.util;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmtest.data.StudentsViewModel;
import com.example.mvvmtest.entity.Student;

/**
 * @author hy
 * @Date 2019/10/25 0025
 **/
public class MyViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StudentsViewModel.class)) {
            return (T) new StudentsViewModel(Student.getDefaultStudents());
        }
        return null;
    }
}
