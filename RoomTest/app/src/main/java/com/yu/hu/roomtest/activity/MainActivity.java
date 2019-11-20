package com.yu.hu.roomtest.activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yu.hu.roomtest.R;
import com.yu.hu.roomtest.adapter.StudentItemDecoration;
import com.yu.hu.roomtest.adapter.StudentListAdapter;
import com.yu.hu.roomtest.entity.Student;
import com.yu.hu.roomtest.repository.StudentRepository2;

import java.util.List;

public class MainActivity extends BaseActivity {

    protected static final String TAG = "MainActivity";


    private StudentRepository2 mStudentRepository;
    private StudentListAdapter mStudentAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void init() {
        Log.d(TAG, "init: ");

        TextView title = findViewById(R.id.tv_title);
        title.setText(getString(R.string.str_stu_list));

        ImageView optionIcon = findViewById(R.id.img_right);
        optionIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionBtnClicked();
            }
        });

        this.mStudentRepository = new StudentRepository2(getApplication());
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new StudentItemDecoration(this, 3));
        this.mStudentAdapter = new StudentListAdapter(this, mStudentRepository);
        recyclerView.setAdapter(mStudentAdapter);

        mStudentRepository.getAllStudents().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> students) {
                mStudentAdapter.submitList(students);
            }
        });
    }

    //添加学生
    private void onOptionBtnClicked() {
        Intent intent = new Intent(this, AddStudentActivity.class);
        intent.putExtra(AddStudentActivity.KEY_START_TYPE, AddStudentActivity.START_TYPE_ADD);
        startActivityForResult(intent, AddStudentActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != AddStudentActivity.REQUEST_CODE) {
            return;
        }
//        if (resultCode == AddStudentActivity.RESULT_CODE_Add) {
//            Student student = null;
//            if (data != null) {
//                student = data.getParcelableExtra(AddStudentActivity.KEY_STUDENT);
//            }
//
//            if (student == null) {
//                mStudentAdapter.setStudentList(mStudentRepository.getAllStudents());
//            } else {
//                mStudentAdapter.addStudent(student);
//            }
//        } else if (resultCode == AddStudentActivity.RESULT_CODE_MODIFY) {
//            mStudentAdapter.setStudentList(mStudentRepository.getAllStudents());
//        }
    }
}
