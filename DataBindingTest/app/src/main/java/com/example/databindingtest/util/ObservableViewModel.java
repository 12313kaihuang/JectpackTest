package com.example.databindingtest.util;

import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.ViewModel;

/**
 * @author hy
 * @Date 2019/10/23 0023
 * <p>
 * An [Observable] [ViewModel] for Data Binding.
 **/
@SuppressWarnings("unused")
public class ObservableViewModel extends ViewModel implements Observable {

    private PropertyChangeRegistry callBacks = new PropertyChangeRegistry();

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callBacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callBacks.remove(callback);
    }

    /**
     * 通知监听器此实例的所有属性均已更改。
     */
    public void notifyChange() {
        callBacks.notifyCallbacks(this, 0, null);
    }

    /**
     * 通知侦听器某个特定属性已更改,属性的getter方法应该被标记为 [Bindable] 来在' BR '中生成一个字段作为' fieldId '使用。
     */
    public void notifyPropertyChanged(int fieldId) {
        callBacks.notifyCallbacks(this, fieldId, null);
    }
}
