package com.example.databindingtest.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModelProvider;

import com.example.databindingtest.BR;
import com.example.databindingtest.R;
import com.example.databindingtest.data.IntervalTimerViewModel;
import com.example.databindingtest.data.IntervalTimerViewModelFactory;
import com.example.databindingtest.databinding.ActivitySecondDemoBinding;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author hy
 * @Date 2019/10/23 0023
 * <p>
 * 数据双向绑定 demo
 **/
public class TwoWayDemoActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_KEY = "timer";

    private IntervalTimerViewModel viewModel = new ViewModelProvider(this,
            new IntervalTimerViewModelFactory()).get(IntervalTimerViewModel.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySecondDemoBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_second_demo);
        dataBinding.setViewmodel(viewModel);

        /* Save the user settings whenever they change */
        observeAndSaveTimePerSet(viewModel.timePerWorkSet, R.string.prefs_timePerWorkSet);
        observeAndSaveTimePerSet(viewModel.timePerRestSet, R.string.prefs_timePerRestSet);

        /* Number of sets needs a different  */
        observeAndSaveNumberOfSets(viewModel);

        if (savedInstanceState == null) {
            /* If this is the first run, restore shared settings */
            restorePreferences(viewModel);
            observeAndSaveNumberOfSets(viewModel);
        }
    }

    private void observeAndSaveTimePerSet(ObservableInt timePerWorkSet, final int prefsKey) {
        timePerWorkSet.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Log.d("saveTimePerWorkSet", "Saving time-per-set preference");

                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
                if (sharedPreferences != null) {
                    sharedPreferences.edit().putInt(getString(prefsKey), ((ObservableInt) sender).get()).commit();
                }
            }
        });
    }

    private void restorePreferences(IntervalTimerViewModel viewModel) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        if (sharedPreferences == null)return;
        String  timePerWorkSetKey = getString(R.string.prefs_timePerWorkSet);
        boolean wasAnythingRestored = false;
        if (sharedPreferences.contains(timePerWorkSetKey)) {
            viewModel.timePerWorkSet.set(sharedPreferences.getInt(timePerWorkSetKey, 100));
            wasAnythingRestored = true;
        }
        String   timePerRestSetKey = getString(R.string.prefs_timePerRestSet);
        if (sharedPreferences.contains(timePerRestSetKey)) {
            viewModel.timePerRestSet.set(sharedPreferences.getInt(timePerRestSetKey, 50));
            wasAnythingRestored = true;
        }
        String   numberOfSetsKey = getString(R.string.prefs_numberOfSets);
        if (sharedPreferences.contains(numberOfSetsKey)) {
            viewModel.setNumberOfSets((ArrayList<Integer>) Arrays.asList(0,sharedPreferences.getInt(numberOfSetsKey, 5)));
            wasAnythingRestored = true;
        }
        if (wasAnythingRestored) Log.d("saveTimePerWorkSet", "Preferences restored");
        viewModel.stopButtonClicked();
    }


    private void observeAndSaveNumberOfSets(IntervalTimerViewModel viewModel) {
        viewModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.numberOfSets) {
                    Log.d("saveTimePerWorkSet", "Saving number of sets preference");

                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
                    if (sharedPreferences != null) {
                        sharedPreferences.edit().putInt(getString(R.string.prefs_numberOfSets), ((ObservableInt) sender).get()).commit();
                    }
                }
            }
        });
    }
}
