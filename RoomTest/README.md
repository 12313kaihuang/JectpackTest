# [RoomTest]( https://developer.android.google.cn/topic/libraries/architecture/room )
Room数据库使用的一个简易Demo

[博客链接](https://www.jianshu.com/p/3948bb7853e5)
---

#### 最终效果图
<img src="gif/untitled.gif" alt="Sample"  width="300" height="480">  

<br/>

**说明**：<br/>
第一次安装进去可能会没有数据展示（会初始化四个学生信息），因为那个数据我是通过`AsyncTask`异步加进去的，会有一个时间差的关系。退出重进一下就好了。
暂时没想到什么巧妙的办法解决，后面还打算看一下`LiveData`和`ViewModel`，原文档中的例子也是带了，使用那个的话应该就不会有这个问题了，所以这里就先留着这个`bug`吧。。
<br/>

下面是博客正文<br/>
---

#### 前言
`Room`是`Architecture Components`（后面简称为`架构组件`）中的一员，官方给他的介绍是：`Room`库在`SQLite`上提供了一个抽象层，允许更健壮的数据库访问，同时利用`SQLite`的全部功能。文档链接：[Room Persistence Library ](https://developer.android.google.cn/topic/libraries/architecture/room)。

下面开始正文。

#### 导入
文档链接：[Room](https://developer.android.google.cn/jetpack/androidx/releases/room)

1. 首先需要在根目录的**`build.gradle`**中加上**`google`**的`maven`仓库依赖。
```gradle
repositories {
      google()  //add Google Maven repository
      jcenter()
}
```
2. 在`app`的**`build.gradle`**中加上需要的依赖
```gradle
def room_version = "2.2.0"  //room版本号
implementation "androidx.room:room-runtime:$room_version"
annotationProcessor "androidx.room:room-compiler:$room_version" // For Kotlin use kapt instead of annotationProcessor

//一般上面两句就可以了，下面是可选项

// optional - Kotlin Extensions and Coroutines support for Room
implementation "androidx.room:room-ktx:$room_version"

// optional - RxJava support for Room
implementation "androidx.room:room-rxjava2:$room_version"

// optional - Guava support for Room, including Optional and ListenableFuture
implementation "androidx.room:room-guava:$room_version"

// Test helpers
testImplementation "androidx.room:room-testing:$room_version"
```

#### 使用
[文档链接](https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0)，下面通过一个`student`表为例来展示如何使用：
1. **创建实体类**
```java
@Entity(tableName = "tb_student")  //指定表名为tb_student
public class Student {

    @PrimaryKey  //标记为主键
    private long id; //id

    @NonNull  //不能为null
    @ColumnInfo(name = "first_name")  //指定列名为first_name
    private String firstName;

    private String lastName;

    private String major;  //专业

    public Student() {
    }

    //省去get、set方法
}
```
**说明**：
* 首先实体类**需要有一个构造函数**，其次存储在数据库中的**每个字段要么是公共的，要么有一个`getter`方法。**
* **`@Entity`**：每个`@Entity`表示表中的一个实体。如果希望表名与类名不同，可通过`tableName `指定表名。
* **`@PrimaryKey`**：标记主键
* **`@NonNull`**：表示参数、字段或方法返回值永远不能为空。
* **`@ColumnInfo`**：指定列名，如果希望列的名称与成员变量的名称不同，可以使用`name`指定表中的列的名称。

2. **创建Dao对象**
```java
@Dao
public interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Student student);

    @Query("DELETE FROM tb_student")
    void deleteAll();

    @Query("SELECT * FROM tb_student ORDER BY id ASC")
    List<Student> getAlphabetizedStudents();
}
```
**说明**：
* 在[DAO](https://developer.android.com/training/data-storage/room/accessing-data.html)（数据访问对象）中，指定SQL查询并将其与方法调用关联。
`DAO`**必须是接口或抽象类**。
* **`@Dao`**：标识为`Room`的`DAO`类。
* **`@Insert`**：插入一个对象，这里可以不写任何`SQL`语句，另外还有**`@Delete`**和**`@Update`**注释，用于删除和更新行。
* **`onConflict = OnConflictStrategy.IGNORE`**：如果冲突字词与列表中已有的字词完全相同，则冲突策略将忽略该字词。另外它还有另外两个可选项：**`OnConflictStrategy.ABORT`**（默认）和**`OnConflictStrategy.REPLACE`**（替换）。
* **`@Query`**：使用`@Query`结合`SQL`语句可以用于读取和复杂的查询。

3. **创建Room Database**
```java
@Database(entities = {Student.class}, version = 1)
public abstract class StudentRoomDatabase extends RoomDatabase {

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
                            .allowMainThreadQueries()  //允许在主线程进行查询操作
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
```
**说明**：
* `Room`类**必须是抽象类**，并且**必须继承`RoomDatabase`**。
* **`@Database`**：注释为`Room`数据库，**`entities`**用于声明该数据库中的实体，**`version`**设置版本号。
* **`allowMainThreadQueries()`**： 默认情况下，所有的查询操作都不能在主线程中进行，这里为了方便使用此方法开启主线程查询。

下面再来简单介绍一个`Room Database`，以下是官方文档对其的介绍的翻译：
`Room`是`SQLite`数据库之上的数据库层。`Room`负责处理您过去使用`SQLiteOpenHelper`处理的普通任务。
* `Room`使用`DAO`向其数据库发出查询。
* 默认情况下，为避免`UI`性能下降，`Room`*不允许您在主线程上发出数据库查询*。[`LiveData`](https://developer.android.com/)通过在需要时自动在后台线程上异步运行查询来应用此规则。
* `Room`提供了`SQLite`语句的编译时检查。
* 您的`Room`类必须是抽象类，并且必须扩展`RoomDatabase`。
* 通常，整个应用程序只需要一个`Room`数据库实例。

4. **创建Repository**
```java
public class StudentRepository {

    private StudentDao mStudentDao;
    private List<Student> mAllStudents;

    public StudentRepository(Application application) {
        StudentRoomDatabase db = StudentRoomDatabase.getDatabase(application);
        mStudentDao = db.studentDao();
        //Room在单独的线程上执行所有查询
        mAllStudents = mStudentDao.getAlphabetizedStudents();
    }

    public List<Student> getAllStudents() {
        return mAllStudents;
    }

    public void insert(Student student) {
        //必须在非UI线程上调用dao的insert方法，否则应用程序将崩溃。
        //所以这里采用了AsyncTask来进行异步的插入操作
        new insertAsyncTask(mStudentDao).execute(student);
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
```
老规矩，下面是官方文档对`repository`的介绍：
`Repository`是一个类，它抽象了对多个数据源的访问。该存储库不是`Architecture Components`的一部分，但是，对于*代码分离和体系结构*来说，这是一个建议的最佳实践。一个`Repository`类处理数据操作。它为应用程序的其余部分提供了干净的`API`，以获取应用程序数据。
`Repository`管理查询线程，并允许您使用多个后端。在最常见的示例中，存储库实现了用于确定是从网络中获取数据还是使用本地数据库中缓存的结果的逻辑。

5. **使用Repository操作数据库**
到了这里，其实整个流程就走完了，需要操作数据库的地方**直接创建上面的`Repository`对象并执行相应方法即可**，所以下面就仅贴出`MainActivity`中的代码（完整代码地址在本文最后）：
```java
//MainActivity.java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StudentRepository mStudentRepository = new StudentRepository(getApplication());
        //数据库操作
        List<Student> students = mStudentRepository.getAllStudents();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //传入数据源并显示
        recyclerView.setAdapter(new StudentAdapter(this,students));
    }
}
```
效果图：
![](https://upload-images.jianshu.io/upload_images/10149931-5de1fcb5c9079641.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/740)

#### 初始化数据库
除了通过`Repository`可以向数据库添加数据意外，我们还**可以在启动应用程序时向数据库中添加一些数据**：
1. **创建一个`RoomDatabase.Callback`**：
```java
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
        mDao.deleteAll();
        Student student = new Student(1, "Tom", "Math");
        mDao.insert(student);
        student = new Student(2, "Bob", "English");
        mDao.insert(student);
        return null;
    }
}
```
2. **创建RoomDatabase是加入这个callback**:
```java
INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
        StudentRoomDatabase.class, "student_database")
        .allowMainThreadQueries()  //允许在主线程进行查询操作
        .addCallback(sRoomDatabaseCallback)  //启动应用程序时删除所有内容并重新填充数据库
        .build();
```

加入后，完整的`StudentRoomDatabase.java`如下：
```java
@Database(entities = {Student.class}, version = 1)
public abstract class StudentRoomDatabase extends RoomDatabase {

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
                            .allowMainThreadQueries()  //允许在主线程进行查询操作
                            .addCallback(sRoomDatabaseCallback)  //启动应用程序时删除所有内容并重新填充数据库
                            .build();
                }
            }
        }
        return INSTANCE;
    }

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
            mDao.deleteAll();
            Student student = new Student(1, "Tom", "Math");
            mDao.insert(student);
            student = new Student(2, "Bob", "English");
            mDao.insert(student);
            return null;
        }
    }
}
```

#### 数据库版本更新
[原文链接](https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929)，文中给出了4种方案，但是在日常开发中，我们自然是要在保留原有数据的同时升级数据库版本了，假设我现在需要给`student`表添加一列`from_where `用于表示生源地，下面开始操作：
1.首先肯定先需要修改我们的`Student`实体类，给它添加一个字段：
```java
@Entity(tableName = "tb_student")  //指定表名为tb_student
public class Student {
    ...

    @ColumnInfo(name = "from_where")
    private String fromWhere;

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    ...
}
```
2. 其次需要修改`RoomDatabase`，先将数据库版本升级，改为2
```java
@Database(entities = {Student.class}, version = 2)
public abstract class StudentRoomDatabase extends RoomDatabase {
    ....
}
```
3. 创建一个`Migration `迁移
```java
//1,2 表示从1升级到2
static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("ALTER TABLE tb_student ADD COLUMN from_where TEXT DEFAULT '未填写'");
    }
};
```

4. 将创建的`RoomDatabase`时将迁移添加进去：
```java
INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StudentRoomDatabase.class, "student_database")
                            .addMigrations(MIGRATION_1_2)  //数据库升级迁移
                            ...
                            .build();
```
我们来看一下`addMigrations`方法的声明:
```java
public Builder<T> addMigrations(@NonNull Migration... migrations)
```
所以如果以后再需要将据库版本升级到`3`的话只需要再创建一个`Migration MIGRATION_2_3`然后`add`进去就可以了。

最后，导出数据库文件并用相关软件打开：
![](https://upload-images.jianshu.io/upload_images/10149931-cd75760a60b5b2cc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
可以看到，生源地字段已成功加入到数据库当中。
