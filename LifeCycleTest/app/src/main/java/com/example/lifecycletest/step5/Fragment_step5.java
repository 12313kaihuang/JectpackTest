package com.example.lifecycletest.step5;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lifecycletest.R;

/**
 * @author hy
 * @Date 2019/11/2 0002
 * <p>
 * Shows a SeekBar that should be synced with a value in a ViewModel.
 **/
public class Fragment_step5 extends Fragment {

    private SeekBar mSeekBar;

    private SeekBarViewModel mSeekBarViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step5, container, false);
        mSeekBar = view.findViewById(R.id.seekBar);

        //Note: You should use the activity as the lifecycle owner, as the lifecycle of each fragment is independent.
        //注意:您应该使用tActivity作为生命周期的所有者，因为每个片段的生命周期是独立的。

        //owner 为 activity  观察的是Activity！ 否则无法实现同步
        mSeekBarViewModel = ViewModelProviders.of(getActivity()).get(SeekBarViewModel.class);
        subscribeSeekBar();

        return view;
    }


    private void subscribeSeekBar() {

        // Update the ViewModel when the SeekBar is changed.
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Log.d("Step5", "Progress changed!");
                    mSeekBarViewModel.seekbarValue.setValue(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Update the SeekBar when the ViewModel is changed.  观察的是Activity！ 否则无法实现同步
        mSeekBarViewModel.seekbarValue.observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer value) {
                if (value != null) {
                    mSeekBar.setProgress(value);
                }
            }
        });
    }
}
