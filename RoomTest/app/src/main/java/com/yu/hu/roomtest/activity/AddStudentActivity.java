package com.yu.hu.roomtest.activity;


import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yu.hu.roomtest.R;
import com.yu.hu.roomtest.entity.Student;
import com.yu.hu.roomtest.repository.StudentRepository;
import com.yu.hu.roomtest.util.Utils;

/**
 * @author hy
 * @Date 2019/10/14 0014
 **/
public class AddStudentActivity extends BaseActivity {

    private static final String TAG = "AddStudentActivity";

    public static final int REQUEST_CODE = 122;

    public static final int RESULT_CODE_Add = 123;
    public static final int RESULT_CODE_MODIFY = 124;

    public static final String KEY_START_TYPE = "key_start_type";
    public static final String KEY_STUDENT = "key_student";

    public static final String START_TYPE_ADD = "start_type_add";
    public static final String START_TYPE_STUDENT_INFO = "start_type_student_info";
    public static final String START_TYPE_MODIFY = "start_type_modify";

    private String mStartType;

    private EditText mEtId;
    private EditText mEtName;
    private EditText mEtMajor;

    private TextView mTvTitle;
    private ImageView mIvOption;
    private Button mAddBtn;

    private StudentRepository mStudentRepository;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_student;
    }

    @Override
    protected void init() {
        this.mStudentRepository = new StudentRepository(getApplication());

        Intent intent = getIntent();
        String startType = intent.getStringExtra(KEY_START_TYPE);
        mStartType = startType;
        initView();
        initStartType(startType);
    }

    private void initView() {
        mTvTitle = findViewById(R.id.tv_title);
        mIvOption = findViewById(R.id.img_right);
        mEtId = findViewById(R.id.et_id);
        mEtName = findViewById(R.id.et_name);
        mEtMajor = findViewById(R.id.et_major);

        mAddBtn = findViewById(R.id.btn_add);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddBtnClick();
            }
        });


        mIvOption.setImageResource(R.drawable.ic_modify);
        mIvOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionBtnClicked();
            }
        });
    }


    private void initStartType(String startType) {
        switch (startType) {
            case START_TYPE_ADD:
                //添加学生
                mTvTitle.setText(getString(R.string.add_stu));
                mIvOption.setVisibility(View.GONE);
                break;
            case START_TYPE_MODIFY:
                //信息修改
            default:
                //查看学生信息
                Intent intent = getIntent();
                Student student;
                if (intent == null || (student = intent.getParcelableExtra(KEY_STUDENT)) == null) {
                    return;
                }
                mTvTitle.setText(student.getFirstName());
                mEtId.setText(String.valueOf(student.getId()));
                mEtId.setEnabled(false);
                mEtName.setText(student.getFirstName());
                mEtName.setEnabled(false);
                mEtMajor.setText(student.getMajor());
                mEtMajor.setEnabled(false);
                mAddBtn.setVisibility(View.GONE);
                break;
        }
        //相当于执行先查看信息然后点击修改按钮
        if (startType.equals(START_TYPE_MODIFY)) onOptionBtnClicked();
    }

    //检查输入是否为空
    private boolean checkInput() {
        if (TextUtils.isEmpty(mEtId.getText().toString())) {
            Utils.showShortToast(this, "学号不能为空");
            return false;
        } else if (!mStartType.equals(START_TYPE_MODIFY) &&
                mStudentRepository.findById(Long.parseLong(mEtId.getText().toString().trim())) != null) {
            Utils.showShortToast(this, "学号已存在");
            return false;
        } else if (TextUtils.isEmpty(mEtName.getText().toString())) {
            Utils.showShortToast(this, "姓名不能为空");
            return false;
        } else if (TextUtils.isEmpty(mEtMajor.getText().toString())) {
            Utils.showShortToast(this, "专业不能为空");
            return false;
        }
        return true;
    }


    //修改
    private void onOptionBtnClicked() {
        mIvOption.setVisibility(View.GONE);
        mAddBtn.setVisibility(View.VISIBLE);
        mEtName.setEnabled(true);
        mEtMajor.setEnabled(true);
        mStartType = START_TYPE_MODIFY;
    }

    //点击按钮
    private void onAddBtnClick() {
        if (!checkInput()) {
            return;
        }
        Student student = null;
        try {
            student = new Student(Long.parseLong(mEtId.getText().toString()), mEtName.getText().toString(), mEtMajor.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onAddBtnClick: create student err", e);
        }
        if (student != null) {
            mStudentRepository.insert(student);
        }

        if (mStartType.equals(START_TYPE_ADD)) {
            Intent intent = new Intent();
            intent.putExtra(KEY_STUDENT, student);
            setResult(RESULT_CODE_Add, intent);
        } else if (mStartType.equals(START_TYPE_MODIFY)) {
            setResult(RESULT_CODE_MODIFY);
        }
        finish();
    }


}
