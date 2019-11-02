package com.example.lifecycletest.step2;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

/**
 * @author hy
 * @Date 2019/10/29 0029
 **/
public class ChronometerViewModel extends ViewModel {

    @Nullable
    private Long starTime;

    @Nullable
    public Long getStarTime() {
        return starTime;
    }

    public void setStarTime(@Nullable Long starTime) {
        this.starTime = starTime;
    }
}
