package com.example.databindingtest.data;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * @author hy
 * @Date 2019/10/21 0021
 **/
public class SimpleViewModel extends ViewModel {

    public MutableLiveData<String> name = new MutableLiveData<>("Ada");
    public MutableLiveData<String> lastName = new MutableLiveData<>("Lovelace");
    public MutableLiveData<Integer> likes = new MutableLiveData<>(0);

    public LiveData<Popularity> popularity = Transformations.map(likes, new Function<Integer, Popularity>() {
        @Override
        public Popularity apply(Integer input) {
            if (input > 9) {
                return Popularity.STAR;
            } else if (input > 4) {
                return Popularity.POPULAR;
            }
            return Popularity.NORMAL;
        }
    });

    public MutableLiveData<Integer> getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes.setValue(likes);
    }

    public void onLike() {
        Integer value = likes.getValue();
        if (value == null) value = 0;
        value = value + 1;
        Log.d("SimpleViewModel", "onLike: value = " + value);
        likes.setValue(value);
    }

    public enum Popularity {
        NORMAL,
        POPULAR,
        STAR
    }
}


