package com.example.lifecycletest.step3;

import android.os.Bundle;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lifecycletest.R;
import com.example.lifecycletest.test.MyViewModel;
import com.example.lifecycletest.test.MyViewModel2;
import com.example.lifecycletest.test.MyViewModelFactory;

/**
 * 使用Observer实现
 */
public class ChronometerActivity3 extends AppCompatActivity {

    private LiveDataTimerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chrono_activity_3);

        viewModel = ViewModelProviders.of(this).get(LiveDataTimerViewModel.class);
        MyViewModel myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        MyViewModel2 myViewModel2 = ViewModelProviders.of(this, new MyViewModelFactory()).get(MyViewModel2.class);
        Log.d("TAG", "onCreate: name = " + myViewModel2.getName());
        subscribe();
    }

    private void subscribe() {
        final Observer<Long> elapsedTimerObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                String newText = ChronometerActivity3.this.getResources().getString(
                        R.string.seconds, aLong
                );
                ((TextView) findViewById(R.id.timer_textview)).setText(newText);
                Log.d("ChronoActivity3", "Updating timer");
            }
        };
        viewModel.getElashedTime().observe(this, elapsedTimerObserver);
    }


}
