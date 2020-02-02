# BottomNavigationTest
`BottomNavigationView`的简单使用及自定义**NavigationView**

简单使用
---
借助`Android Studio`新建项目时的`Bottom Navigation Activity`模板以及[官方文档](https://developer.android.google.cn/guide/navigation/navigation-ui#bottom_navigation)实现`BottomNavigationView`的简单使用。

1. 首先引入`Navigation`:
   ```groovy
   dependencies {
        ...
        
        //navigation
        implementation 'androidx.navigation:navigation-fragment:2.2.0'
        implementation 'androidx.navigation:navigation-ui:2.2.0'
    }
   ```

2. 创建对应的`navigation`文件`nav_graph.xml`：
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <navigation xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:id="@+id/nav_graph"
        app:startDestination="@id/homeFragment">
    
       <fragment
           android:id="@+id/navigation_home"
           android:name="com.example.bottomnavigationtest.ui.HomeFragment"
           android:label="HomeFragment"
           tools:layout="@layout/fragment_test" />
        
       <fragment
           android:id="@+id/navigation_dashboard"
           android:name="com.example.bottomnavigationtest.ui.DashboardFragment"
           android:label="DashboardFragment" />
        
       <fragment
           android:id="@+id/navigation_notifications"
           android:name="com.example.bottomnavigationtest.ui.NotificationFragment"
           android:label="NotificationFragment" />
    </navigation>
    ```
3. 创建底部导航栏按钮文件`bottom_nav_menu.xml`，注意这里的**id要与** `nav_graph.xml`中**对应的`fragment`一致**。
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <menu xmlns:android="http://schemas.android.com/apk/res/android">
        <item
            android:id="@+id/navigation_home"
            android:icon="@drawable/ic_home_black_24dp"
            android:title="Home" />
    
        <item
            android:id="@+id/navigation_dashboard"
            android:icon="@drawable/ic_dashboard_black_24dp"
            android:title="Dashboard" />
    
        <item
            android:id="@+id/navigation_notifications"
            android:icon="@drawable/ic_notifications_black_24dp"
            android:title="Notifications" />
    
    </menu>
    ```
3. 创建相应`MainActivity`布局 **`activity_main.xml`**
   ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">
    
        <!-- app:navGraph="@navigation/nav_graph" 关联navigation导航 -->
        <fragment
            android:id="@+id/nav_host_fragment"
            android:name="androidx.navigation.fragment.NavHostFragment"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:defaultNavHost="true"
            app:layout_constraintBottom_toTopOf="@id/nav_view"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:navGraph="@navigation/nav_graph" />
    
       <!-- app:menu="@menu/bottom_nav_menu" 关联底部导航item -->
       <com.google.android.material.bottomnavigation.BottomNavigationView
           android:id="@+id/nav_view"
           android:layout_width="0dp"
           android:layout_height="wrap_content"
           android:layout_marginStart="0dp"
           android:layout_marginEnd="0dp"
           android:background="?android:attr/windowBackground"
           app:layout_constraintBottom_toBottomOf="parent"
           app:layout_constraintLeft_toLeftOf="parent"
           app:layout_constraintRight_toRightOf="parent"
           app:menu="@menu/bottom_nav_menu" />
    
    </androidx.constraintlayout.widget.ConstraintLayout>
   ```
5. 将`Navigayion`与`NavController`相关联
    ```java
    public class MainActivity extends AppCompatActivity {
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //BottomNavigationView
            BottomNavigationView navigationView = findViewById(R.id.nav_view);
            //构建导航id  menu 与 navigation中的id要相对应
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                    .build();
            final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            //与navcontroller相关联
            NavigationUI.setupWithNavController(navigationView, navController);
    
            //设置点击事件
            navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //跳转相应fragment
                    navController.navigate(menuItem.getItemId());
                    //返回false会有一个点击悬浮效果，返回true则不会有该效果
                    return false;
                }
            });
        }
    }
    
    ```
    
自定义`BottomNavigationView`
---
最近是新型冠状病毒疫情期，不难发现很多应用都多出了一个**抗击疫情**或类似的专题，例如头条就多了**抗击肺炎**的模块，底部的导航菜单栏也发生变化，说明这些应该是后台动态下发的，然后再根据下发的菜单栏显示对应的模块，而上面的底部导航菜单栏是写死的，所以很有必要自定义一个`BottomNavigationView`来应对这种情况。


参考文章
----
* [导航组件](https://developer.android.google.cn/guide/navigation/navigation-ui#bottom_navigation)
* [Codelable-Jetpack Navigation](https://codelabs.developers.google.com/codelabs/android-navigation/index.html?index=..%2F..%2Findex#8)
* [Jetpack全组件实战](https://coding.imooc.com/learn/list/402.html)
