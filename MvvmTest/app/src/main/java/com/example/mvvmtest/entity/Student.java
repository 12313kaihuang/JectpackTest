package com.example.mvvmtest.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名：  com.yu.hu.roomtest.entity
 * 文件名：Student
 * 创建者：HY
 * 创建时间：2019/10/13 17:42
 * 描述：  TODO
 */
@SuppressWarnings("unused")
public class Student implements Parcelable {

    private long id;

    private String firstName;

    private String lastName;

    private String major;

    private String fromWhere;

    public Student() {
    }

    public Student(long id, @NonNull String firstName, String major) {
        this.id = id;
        this.firstName = firstName;
        this.major = major;
    }

    public static List<Student> getDefaultStudents(){
        List<Student> defaultList = new ArrayList<>();
        Student student = new Student(1, "Tom", "Math");
        defaultList.add(student);
        student = new Student(2, "Bob", "English");
        defaultList.add(student);
        student = new Student(3, "Marry", "Computer");
        defaultList.add(student);
        student = new Student(4, "Peter", "Computer");
        defaultList.add(student);
        return defaultList;
    }

    private Student(Parcel in) {
        id = in.readLong();
        firstName = in.readString();
        lastName = in.readString();
        major = in.readString();
        fromWhere = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(major);
        dest.writeString(fromWhere);
    }
}
