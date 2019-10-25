package com.example.mvvmtest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.mvvmtest.R;
import com.example.mvvmtest.adapter.StudentAdapter;
import com.example.mvvmtest.adapter.StudentItemDecoration;
import com.example.mvvmtest.data.StudentsViewModel;
import com.example.mvvmtest.data.util.MyViewModelFactory;
import com.example.mvvmtest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        StudentsViewModel viewModel = new ViewModelProvider(this, new MyViewModelFactory()).get(StudentsViewModel.class);
        mBinding.setViewModel(viewModel);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.addItemDecoration(new StudentItemDecoration(this, 3));
        mBinding.recyclerView.setAdapter(new StudentAdapter(this, viewModel));
        mBinding.actionBarLayout.setTitle("学生列表");
        mBinding.actionBarLayout.setDrawableRight(R.drawable.ic_add);
    }

}
