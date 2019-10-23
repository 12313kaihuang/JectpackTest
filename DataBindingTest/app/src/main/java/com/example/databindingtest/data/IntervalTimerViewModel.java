package com.example.databindingtest.data;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;

import com.example.databindingtest.BR;
import com.example.databindingtest.util.ObservableViewModel;
import com.example.databindingtest.util.TimerWrapper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author hy
 * @Date 2019/10/23 0023
 * <p>
 * 包含控制 work/rest 间隔计时器的逻辑的ViewModel
 * <p>
 * 它使用不同的方式为视图公开数据:
 * * Observable Fields  可观察到的字段
 * * Bindable properties 绑定属性
 * * Methods for user actions 用户操作方法
 * <p>
 * 数据绑定特性:
 *
 * <b>单向数据绑定</b>
 * <p>
 * See [workTimeLeft] and [restTimeLeft]. 它们从ViewModel中更新 and  UI会自动刷新
 * <p>
 * 单向也适用于用户操作。 See [workTimeIncrease] and 一个lambda表达式在布局中是如何构建的:
 * <p>
 * `android:onClick="@{() -> viewmodel.workTimeIncrease()}"`
 *
 *
 * <b>双向数据绑定</b>
 * <p>
 * * Simple: See `timerRunning`. It toggles between start and pause with a single line on the  layout:
 * <p>
 * `android:checked="@={viewmodel.timerRunning}"`
 * <p>
 * * Custom: [numberOfSets] uses the most advanced form of 2-way Data Binding.
 * See [com.example.android.databinding.twowaysample.ui.NumberOfSetsBindingAdapters].
 */
public class IntervalTimerViewModel extends ObservableViewModel {

    private static final int INITIAL_SECONDS_PER_WORK_SET = 5;  // Seconds
    private static final int INITIAL_SECONDS_PER_REST_SET = 2;  // Seconds
    private static final int INITIAL_NUMBER_OF_SETS = 5;


    public ObservableInt timePerWorkSet = new ObservableInt(INITIAL_SECONDS_PER_WORK_SET * 10);// tenths
    public ObservableInt timePerRestSet = new ObservableInt(INITIAL_SECONDS_PER_REST_SET * 10); // tenths
    public ObservableInt workTimeLeft = new ObservableInt(timePerWorkSet.get()); // tenths
    public ObservableInt restTimeLeft = new ObservableInt(timePerRestSet.get()); // tenths

    private TimerWrapper.DefaultTimer timer;

    public IntervalTimerViewModel(TimerWrapper.DefaultTimer timer) {
        this.timer = timer;
    }

    private boolean timerRunning;

    @Bindable
    public boolean getTimerRunning() {
        timerRunning = state == TimerStates.STARTED;
        return timerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
        // These methods take care of calling notifyPropertyChanged()
        //if (timerRunning) startButtonClicked() else pauseButtonClicked()
    }

    /**
     * Pause the timer
     */
//    private void pauseButtonClicked() {
//        if (state == TimerStates.STARTED) {
//            startedToPaused()
//        }
//        notifyPropertyChanged(BR.timerRunning)
//    }

    /* 使用自定义双向数据绑定方法处理集合的数量。
    与此属性绑定的EditText将显示一个不同于的字符串 ("Sets: 3/5") which is different from
    the number the user will input (5).

    属性公开一个getter，而视图通过setter更改值。使用@Bindable创建一个数据绑定属性，但它不会自动更新，
    当它改变时需要调用' notifyPropertyChanged(BR.numberOfSets) '它
    */

    private int numberOfSetsTotal = INITIAL_NUMBER_OF_SETS;
    private int numberOfSetsElapsed = 0;

    private ArrayList<Integer> numberOfSets = new ArrayList<>();

    @Bindable
    public ArrayList<Integer> getNumberOfSets() {
        numberOfSets = (ArrayList<Integer>) Arrays.asList(numberOfSetsElapsed, numberOfSetsTotal);
        return numberOfSets;
    }

    public void setNumberOfSets(ArrayList<Integer> numberOfSets) {
        // Only the second Int is being set
        Integer newTotal = numberOfSets.get(1);
        if (newTotal.equals(numberOfSets.get(1))) return;  //如果值没有改变就退出循环调用
        // 只有在不影响当前操作的情况下才进行更新
        if (newTotal != 0 && newTotal > numberOfSetsElapsed) {
            this.numberOfSets = numberOfSets;
            numberOfSetsTotal = newTotal;
        }
        // 即使输入为空，也要强制刷新视图
        notifyPropertyChanged(BR.numberOfSets);
    }

    private TimerStates state = TimerStates.STOPPED;
    private StartedStages stage = StartedStages.WORKING;

    /**
     * Resets timers and state. Called from the UI.
     */
    public void stopButtonClicked() {
        resetTimers();
        numberOfSetsElapsed = 0;
        state = TimerStates.STOPPED;
        stage = StartedStages.WORKING ;// Reset for the next set
        timer.reset();

//        notifyPropertyChanged(BR.timerRunning);
//        notifyPropertyChanged(BR.inWorkingStage);
        notifyPropertyChanged(BR.numberOfSets);
    }

    private void resetTimers() {
        // Reset counters
        workTimeLeft.set(timePerWorkSet.get());

        // Set the start time
        restTimeLeft.set(timePerRestSet.get());
    }


    enum TimerStates {STOPPED, STARTED, PAUSED}

    enum StartedStages {WORKING, RESTING}
}
