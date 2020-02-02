package com.example.bottomnavigationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //简单使用
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
        //配置ActionBar标题对应相应fragment的title
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //navigationView与navcontroller相关联
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
