package com.example.lifecycletest.step6;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;

import com.example.lifecycletest.R;

/**
 * @author hy
 * @Date 2019/11/2 0002
 **/
public class SavedStateActivity extends AppCompatActivity {

    private SavedStateViewModel mSavedStateViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_state_activity);

        // 获取ViewModel，传入一个可选的SavedStateVMFactory,这样你就可以使用SavedStateHandle了
        mSavedStateViewModel = ViewModelProviders.of(this, new SavedStateVMFactory(this))
                .get(SavedStateViewModel.class);

        // Show the ViewModel property's value in a TextView
        mSavedStateViewModel.getName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String savedString) {
                ((TextView) findViewById(R.id.saved_vm_tv))
                        .setText(getString(R.string.saved_in_vm, savedString));

            }
        });

        // Save button
        findViewById(R.id.save_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = ((EditText) findViewById(R.id.name_et)).getText().toString();
                mSavedStateViewModel.saveNewName(newName);
            }
        });
    }
}
