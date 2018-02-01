package com.pravin.android.studentdatabase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pravin.android.studentdatabase.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pravin on 1/2/18.
 */

public class StudentDbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_TYPE_LOADING = 0;
    private final int ITEM_TYPE_STUDENT = 1;
    private List<Student> mStudentList = new ArrayList<>();
    private Context mContext;

    public StudentDbAdapter(List<Student> list, Context context) {
        this.mStudentList = new ArrayList<>(list);
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case ITEM_TYPE_LOADING:
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading_progress, parent);
                return new LoadingProgressViewHolder(view);
            case ITEM_TYPE_STUDENT:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_student, parent);
                return new StudentItemViewHolder(view);
            default:
                // viewType default case
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StudentItemViewHolder) {
            ((StudentItemViewHolder) holder).setStudentData(position);
        } else if (holder instanceof LoadingProgressViewHolder) {
            ((LoadingProgressViewHolder) holder).setProgressBar();
        }

    }

    @Override
    public int getItemCount() {
        return mStudentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        return null == mStudentList.get(position) ? ITEM_TYPE_LOADING : ITEM_TYPE_STUDENT;
    }


    public class LoadingProgressViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgressBar;

        public LoadingProgressViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.pb_loading);
        }

        public void setProgressBar() {
            // TODO: Implement progress bar

        }
    }

    public class StudentItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mRollNo;
        private TextView mStudentName;

        public StudentItemViewHolder(View itemView) {
            super(itemView);
            mRollNo = itemView.findViewById(R.id.tv_roll_no);
            mStudentName = itemView.findViewById(R.id.tv_student_name);
        }

        public void setStudentData(int position) {
            mRollNo.setText(mStudentList.get(position).getRollNo());
            mStudentName.setText(mStudentList.get(position).getName());
        }
    }


}
