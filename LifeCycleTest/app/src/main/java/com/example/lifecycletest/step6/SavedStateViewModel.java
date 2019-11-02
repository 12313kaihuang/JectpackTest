package com.example.lifecycletest.step6;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

/**
 * @author hy
 * @Date 2019/11/2 0002
 **/
public class SavedStateViewModel extends ViewModel {

    private static final String NAME_KEY = "name";

    private SavedStateHandle mState;

    public SavedStateViewModel(SavedStateHandle state) {
        this.mState = state;
    }

    // 公开一个不可变的LiveData
    LiveData<String> getName() {
        // getLiveData获取一个与LiveData中包装的键相关联的对象
        // 所以可以观察到变化.
        return mState.getLiveData(NAME_KEY);
    }

    void saveNewName(String newName) {
        // Sets a new value for the object associated to the key. There's no need to set it
        // as a LiveData.
        mState.set(NAME_KEY, newName);
    }
}
