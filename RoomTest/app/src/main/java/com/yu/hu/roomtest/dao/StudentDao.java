package com.yu.hu.roomtest.dao;

import com.yu.hu.roomtest.entity.Student;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Flowable;

/**
 * 项目名：RoomTest
 * 包名：  com.yu.hu.roomtest.dao
 * 文件名：StudentDao
 * 创建者：HY
 * 创建时间：2019/10/13 18:55
 * 描述：  TODO
 */
@Dao
public interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Student student);

    @Query("DELETE FROM tb_student")
    void deleteAll();

    @Query("DELETE FROM tb_student WHERE id = :id")
    void delete(long id);

    @Query("SELECT * FROM tb_student ORDER BY id ASC")
    List<Student> getAlphabetizedStudents();

    @Query("SELECT * FROM tb_student ORDER BY id ASC")
    LiveData<List<Student>> getLiveStudents();

    @Query("SELECT * FROM tb_student WHERE id = :id")
    Student findById(long id);

    @Query("SELECT * FROM tb_student WHERE id = :id")
    Flowable<Student> findStudnetById(long id);
}
