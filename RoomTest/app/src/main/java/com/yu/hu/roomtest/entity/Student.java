package com.yu.hu.roomtest.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 项目名：RoomTest
 * 包名：  com.yu.hu.roomtest.entity
 * 文件名：Student
 * 创建者：HY
 * 创建时间：2019/10/13 17:42
 * 描述：  TODO
 */
@Entity(tableName = "tb_student")  //指定表名为tb_student
public class Student implements Parcelable {

    @PrimaryKey  //标记位主键
    private long id;

    @NonNull  //不能为null
    @ColumnInfo(name = "first_name")  //指定列名为first_name
    private String firstName;

    private String lastName;

    private String major;

    @ColumnInfo(name = "from_where")
    private String fromWhere;

    public Student() {
    }

    public Student(long id, @NonNull String firstName, String major) {
        this.id = id;
        this.firstName = firstName;
        this.major = major;
    }

    protected Student(Parcel in) {
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj instanceof Student) {
            Student student = (Student) obj;
            if (this == student) return true;
            return id == student.id
                    && firstName.equals(student.firstName)
                    && lastName.equals(student.lastName)
                    && major.equals(student.major)
                    && fromWhere.equals(student.fromWhere);
        } else {
            return false;
        }
    }
}
