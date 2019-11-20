package com.yu.hu.roomtest.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.yu.hu.roomtest.dao.StudentDao;
import com.yu.hu.roomtest.db.StudentRoomDatabase;
import com.yu.hu.roomtest.entity.Student;

import java.util.List;

/**
 * 项目名：RoomTest
 * 包名：  com.yu.hu.roomtest.repository
 * 文件名：StudentRepository
 * 创建者：HY
 * 创建时间：2019/10/13 19:38
 * 描述：  TODO
 */
public class StudentRepository2 {

    private static final String TAG = "StudentRepository";

    private StudentDao mStudentDao;

    public StudentRepository2(Application application) {
        StudentRoomDatabase db = StudentRoomDatabase.getDatabase(application);
        mStudentDao = db.studentDao();
    }

    public List<Student> getAllStudents() {
        List<Student> students = mStudentDao.getAlphabetizedStudents();
        Log.d(TAG, "getAllStudents: students.size = " + students.size() + ", " + students);
        return students;
    }

    public void insert(Student student) {
        //必须在非UI线程上调用dao的insert方法，否则您的应用程序将崩溃。
        //所以这里采用了AsyncTask来进行插入操作
        //new insertAsyncTask(mStudentDao).execute(student);
        Log.d(TAG, "insert: student = " + student);
        mStudentDao.insert(student);
    }

    public Student findById(long id) {
        return mStudentDao.findById(id);
    }

    public void delete(Student student) {
        mStudentDao.delete(student.getId());
    }

    private static class mAsyncTask extends AsyncTask<Runnable, Void, Void> {
        @Override
        protected Void doInBackground(Runnable... runnables) {
            runnables[0].run();
            return null;
        }
    }


    private static class insertAsyncTask extends AsyncTask<Student, Void, Void> {

        private StudentDao mAsyncTaskDao;

        insertAsyncTask(StudentDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            mAsyncTaskDao.insert(students[0]);
            return null;
        }
    }
}
