package com.example.mvvmtest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmtest.R;
import com.example.mvvmtest.data.StudentsViewModel;
import com.example.mvvmtest.databinding.ItemStudentBinding;
import com.example.mvvmtest.entity.Student;
import com.example.mvvmtest.util.Utils;

import java.util.List;

/**
 * 项目名：RoomTest
 * 包名：  com.yu.hu.roomtest
 * 文件名：StudentAdapter
 * 创建者：HY
 * 创建时间：2019/10/13 20:23
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private static final String TAG = "StudentAdapter";

    private Context mContext;
    private StudentsViewModel studentsViewModel;
    private List<Student> mStudentList;
    private LayoutInflater mLayoutInflater;


    public StudentAdapter(Context context, StudentsViewModel studentsViewModel) {
        this.mContext = context;
        this.studentsViewModel = studentsViewModel;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mStudentList = studentsViewModel.getStudentList().getValue();
    }

//    public void setStudentList(List<Student> mStudentList) {
//        this.mStudentList = mStudentList;
//        notifyDataSetChanged();
//    }
//
//    public void addStudent(Student student) {
//        if (mStudentList == null) mStudentList = new ArrayList<>();
//        mStudentList.add(student);
//        notifyItemInserted(mStudentList.size() - 1);  //这里的位置是被插入的item的位置
//        notifyItemRangeChanged(mStudentList.size() - 1, 1); //防止数据错乱
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStudentBinding binding = ItemStudentBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Student student = mStudentList.get(position);
        holder.itemBinding.setStudent(student);

        final int index = position % 5 + 1;
        int resId = mContext.getResources().getIdentifier("default_icon" + index, "drawable", mContext.getPackageName());
        if (resId != 0) {
            Drawable drawable = mContext.getResources().getDrawable(resId);
            Bitmap roundBitmap = Utils.getRoundBitmap(Utils.getBitmapFromDrawable(drawable), 30);
            holder.itemBinding.imageView.setImageBitmap(roundBitmap);
        } else {
            Log.d(TAG, "onBindViewHolder: name = " + "default_icon" + index + " drawable = null");
            holder.itemBinding.imageView.setImageResource(R.drawable.default_icon1);
        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpToAddActivity(student);
//            }
//        });
//
//        final int fPosition = position;
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                showDialog(student, fPosition);
//                return true;
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return mStudentList == null ? 0 : mStudentList.size();
    }

    /**
     * 跳转至相应页面
     */
//    private void jumpToAddActivity(Student student) {
//        Intent intent = new Intent(mContext, AddStudentActivity.class);
//        intent.putExtra(AddStudentActivity.KEY_START_TYPE, AddStudentActivity.START_TYPE_STUDENT_INFO);
//        intent.putExtra(AddStudentActivity.KEY_STUDENT, student);
//        if (mContext instanceof MainActivity) {
//            MainActivity activity = (MainActivity) mContext;
//            activity.startActivityForResult(intent, AddStudentActivity.REQUEST_CODE);
//        }
//    }

    /**
     * dialog
     */
//    private void showDialog(final Student student, final int position) {
//        Log.d(TAG, "showDialog: position = " + position);
//        new CustomDialog(mContext)
//                .setOnModifyBtnClickListener(new CustomDialog.onBtnClickListener() {
//                    @Override
//                    public void onBtnClicked(View view) {
//                        Intent intent = new Intent(mContext, AddStudentActivity.class);
//                        intent.putExtra(AddStudentActivity.KEY_STUDENT, student);
//                        intent.putExtra(AddStudentActivity.KEY_START_TYPE, AddStudentActivity.START_TYPE_MODIFY);
//                        if (mContext instanceof MainActivity) {
//                            MainActivity activity = (MainActivity) mContext;
//                            activity.startActivityForResult(intent, AddStudentActivity.REQUEST_CODE);
//                        }
//                    }
//                })
//                .setOnDeleteBtnClickListener(new CustomDialog.onBtnClickListener() {
//                    @Override
//                    public void onBtnClicked(View view) {
//                        mStudentRepository.delete(student);
//                        mStudentList.remove(position);
//                        notifyItemRemoved(position);
//                        Log.d(TAG, "onBtnClicked: " + (mStudentList.size() - position + 1));
//                        notifyItemRangeChanged(position, mStudentList.size() - position + 1);
//                        Utils.showShortToast(mContext, "删除 " + student.getFirstName());
//                    }
//                })
//                .show();
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemStudentBinding itemBinding;

        ViewHolder(@NonNull ItemStudentBinding itemStudentBinding) {
            super(itemStudentBinding.getRoot());
            this.itemBinding = itemStudentBinding;
        }
    }
}
