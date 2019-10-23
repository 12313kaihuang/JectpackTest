package com.yu.hu.roomtest.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.yu.hu.roomtest.dao.StudentDao;
import com.yu.hu.roomtest.entity.Student;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

/**
 * 项目名：RoomTest
 * 包名：  com.yu.hu.roomtest.db
 * 文件名：StudentRoomDatabase
 * 创建者：HY
 * 创建时间：2019/10/13 19:17
 * 描述：  TODO
 */
@Database(entities = {Student.class}, version = 2)
public abstract class StudentRoomDatabase extends RoomDatabase {

    private static final String TAG = "StudentRoomDatabase";

    //定义与数据库一起使用的DAO。为每个@Dao提供一个抽象的“ getter”方法。
    public abstract StudentDao studentDao();

    //设置为单例，防止同时打开多个数据库实例
    private static volatile StudentRoomDatabase INSTANCE;

    public static StudentRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StudentRoomDatabase.class) {
                if (INSTANCE == null) {
                    //创建一个对象StudentRoomDatabase并将其命名"student_database"
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StudentRoomDatabase.class, "student_database")
                            .addMigrations(MIGRATION_1_2)
                            .allowMainThreadQueries()  //允许在主线程进行查询操作
                            .addCallback(sRoomDatabaseCallback)  //启动应用程序时删除所有内容并重新填充数据库
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //1,2 表示从1升级到2
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tb_student ADD COLUMN from_where TEXT DEFAULT '未填写'");
        }
    };

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            //同理，插入操作不能在主线程中进行，所以这里使用了AsyncTask
            new PopulateDbAsync(INSTANCE).execute();

        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final StudentDao mDao;

        PopulateDbAsync(StudentRoomDatabase db) {
            this.mDao = db.studentDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<Student> students = mDao.getAlphabetizedStudents();
            if (students.size() == 0) {
                Log.d(TAG, "onOpen: 插入操作");
                Student student = new Student(1, "Tom", "Math");
                mDao.insert(student);
                student = new Student(2, "Bob", "English");
                mDao.insert(student);
                student = new Student(3, "Marry", "Computer");
                mDao.insert(student);
                student = new Student(4, "Peter", "Computer");
                mDao.insert(student);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "onPostExecute: 执行完毕");
            super.onPostExecute(aVoid);
        }
    }
}
