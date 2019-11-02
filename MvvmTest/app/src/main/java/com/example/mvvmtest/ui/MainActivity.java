package com.example.mvvmtest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mvvmtest.R;
import com.example.mvvmtest.adapter.StudentAdapter;
import com.example.mvvmtest.adapter.StudentItemDecoration;
import com.example.mvvmtest.data.StudentsViewModel;
import com.example.mvvmtest.data.util.MyViewModelFactory;
import com.example.mvvmtest.databinding.ActivityMainBinding;
import com.example.mvvmtest.entity.Student;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        final StudentsViewModel viewModel = new ViewModelProvider(this, new MyViewModelFactory()).get(StudentsViewModel.class);
        final List<Student> studentList = viewModel.getStudentList().getValue();

        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(viewModel);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.addItemDecoration(new StudentItemDecoration(this, 3));
        mBinding.recyclerView.setAdapter(new StudentAdapter(this, viewModel));
        mBinding.actionBarLayout.setTitle("学生列表");
        mBinding.actionBarLayout.setDrawableRight(R.drawable.ic_add);
        mBinding.actionBarLayout.imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentList.add(new Student(1, "Tom", "Math"));
                viewModel.setStudentList(studentList);
                Toast.makeText(MainActivity.this,"size = "+studentList.size(),Toast.LENGTH_SHORT).show();
            }
        });


    }

}
