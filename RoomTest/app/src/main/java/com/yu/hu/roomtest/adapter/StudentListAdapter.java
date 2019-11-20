package com.yu.hu.roomtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.hu.roomtest.R;
import com.yu.hu.roomtest.activity.AddStudentActivity;
import com.yu.hu.roomtest.activity.MainActivity;
import com.yu.hu.roomtest.dialog.CustomDialog;
import com.yu.hu.roomtest.entity.Student;
import com.yu.hu.roomtest.repository.StudentRepository2;
import com.yu.hu.roomtest.util.Utils;

/**
 * 项目名：RoomTest
 * 包名：  com.yu.hu.roomtest
 * 文件名：StudentAdapter
 * 创建者：HY
 * 创建时间：2019/10/13 20:23
 * <p>
 * 使用{@link ListAdapter}配合Room更好的实现
 */
public class StudentListAdapter extends ListAdapter<Student, StudentListAdapter.ViewHolder> {

    private static final String TAG = "StudentAdapter";

    private Context mContext;
    private StudentRepository2 mStudentRepository;

    public StudentListAdapter(Context context, StudentRepository2 studentRepository) {
        super(new DiffUtil.ItemCallback<Student>() {
            @Override
            public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.mContext = context;
        this.mStudentRepository = studentRepository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Student student = getItem(position);
        holder.mTvId.setText(mContext.getString(R.string.stu_id, student.getId()));
        holder.mTvName.setText(student.getFirstName());
        holder.mTvMajor.setText(student.getMajor());


        final int index = position % 5 + 1;
        int resId = mContext.getResources().getIdentifier("default_icon" + index, "drawable", mContext.getPackageName());
        if (resId != 0) {
            Drawable drawable = mContext.getDrawable(resId);
            Bitmap roundBitmap = Utils.getRoundBitmap(Utils.getBitmapFromDrawable(drawable), 30);
            holder.mIcon.setImageBitmap(roundBitmap);
        } else {
            Log.d(TAG, "onBindViewHolder: name = " + "default_icon" + index + " drawable = null");
            holder.mIcon.setImageResource(R.drawable.default_icon1);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToAddActivity(student);
            }
        });

        final int fPosition = position;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog(student, fPosition);
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    /**
     * 跳转至相应页面
     */
    private void jumpToAddActivity(Student student) {
        Intent intent = new Intent(mContext, AddStudentActivity.class);
        intent.putExtra(AddStudentActivity.KEY_START_TYPE, AddStudentActivity.START_TYPE_STUDENT_INFO);
        intent.putExtra(AddStudentActivity.KEY_STUDENT, student);
        if (mContext instanceof MainActivity) {
            MainActivity activity = (MainActivity) mContext;
            activity.startActivityForResult(intent, AddStudentActivity.REQUEST_CODE);
        }
    }

    /**
     * dialog
     */
    private void showDialog(final Student student, final int position) {
        Log.d(TAG, "showDialog: position = " + position);
        new CustomDialog(mContext)
                .setOnModifyBtnClickListener(new CustomDialog.onBtnClickListener() {
                    @Override
                    public void onBtnClicked(View view) {
                        Intent intent = new Intent(mContext, AddStudentActivity.class);
                        intent.putExtra(AddStudentActivity.KEY_STUDENT, student);
                        intent.putExtra(AddStudentActivity.KEY_START_TYPE, AddStudentActivity.START_TYPE_MODIFY);
                        if (mContext instanceof MainActivity) {
                            MainActivity activity = (MainActivity) mContext;
                            activity.startActivityForResult(intent, AddStudentActivity.REQUEST_CODE);
                        }
                    }
                })
                .setOnDeleteBtnClickListener(new CustomDialog.onBtnClickListener() {
                    @Override
                    public void onBtnClicked(View view) {
                        mStudentRepository.delete(student);
                        Utils.showShortToast(mContext, "删除 " + student.getFirstName());
                    }
                })
                .show();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvId;
        private TextView mTvName;
        private TextView mTvMajor;
        private ImageView mIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvId = itemView.findViewById(R.id.tv_id);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvMajor = itemView.findViewById(R.id.tv_major);
            mIcon = itemView.findViewById(R.id.imageView);
        }
    }
}
