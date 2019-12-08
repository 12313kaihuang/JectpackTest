package com.yu.hu.navigationtest.blank;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yu.hu.navigationtest.R;
import com.yu.hu.navigationtest.databinding.BlankFragmentBinding;

public class BlankFragment extends Fragment {

    private BlankViewModel mViewModel;
    private BlankFragmentBinding mDataBinding;
    private View mRootView;

    public static BlankFragment newInstance() {
        return new BlankFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mDataBinding = BlankFragmentBinding.inflate(inflater, container, false);
        mRootView = mDataBinding.getRoot();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BlankViewModel.class);

        Bundle arguments = getArguments();
        initArguments(arguments != null ? arguments : new Bundle());
    }

    private void initArguments(@NonNull Bundle arguments) {
        int mode = arguments.getInt("mode", 0);
        String text;
        if (mode == 0) {
            //通过bundle传参
            text = arguments.getString("text");
            mDataBinding.textView.setText(String.format("通过bundle传递 ：%s", text));
        } else {
            text = BlankFragmentArgs.fromBundle(arguments).getText();
            //通过Safe Args传参
            mDataBinding.textView.setText(String.format("通过Safe Args ：%s", text));
        }
    }

}
