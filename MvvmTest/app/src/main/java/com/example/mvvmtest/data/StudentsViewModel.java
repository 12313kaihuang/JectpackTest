package com.example.mvvmtest.data;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmtest.entity.Student;

import java.util.List;

/**
 * @author hy
 * @Date 2019/10/25 0025
 **/
public class StudentsViewModel extends ViewModel {

    private static StudentsViewModel INSTANCE;

    private String name;

    private MutableLiveData<List<Student>> studentList;

    public StudentsViewModel(List<Student> defaultStudents) {
        this.studentList = new MutableLiveData<>(defaultStudents);
    }

    public MutableLiveData<List<Student>> getStudentList() {
        //这里还是要返回LiveData，否则视图中的绑定值变化不会生效
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        //要用setValue！
        this.studentList.setValue(studentList);
    }
}
