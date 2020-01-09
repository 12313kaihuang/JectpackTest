package com.yu.hu.navigationtest;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yu.hu.navigationtest.databinding.FragmentMainBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private FragmentMainBinding mDataBinding;
    private View mRootView;
    

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDataBinding = FragmentMainBinding.inflate(inflater, container, false);
        mRootView = mDataBinding.getRoot();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //通过bundle传递参数
        mDataBinding.toBlankBtn.setOnClickListener(v -> {
            //通过Bundle传递数据
            Bundle bundle = new Bundle();
            bundle.putInt("mode", 0);
            bundle.putString("text", mDataBinding.editText.getText().toString());
            Navigation.findNavController(mRootView)
                    //.navigate(R.id.to_blank); //不传值
                    .navigate(R.id.to_blank, bundle);
        });

        //通过safeArgs传参
        mDataBinding.toBlankBtn2.setOnClickListener(v -> {

            //如果args中设置了defaultValue，咋不需要必传
            //如果没设置，则必须要传  见nav_graph.xml
            MainFragmentDirections.ToBlank toBlank =
                    MainFragmentDirections.toBlank(mDataBinding.editText.getText().toString())
                            .setMode(1);
            Navigation.findNavController(mRootView)
                    .navigate(toBlank);
        });
    }
}
