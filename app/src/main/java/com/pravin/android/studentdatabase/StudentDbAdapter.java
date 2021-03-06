package com.pravin.android.studentdatabase;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pravin.android.studentdatabase.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentDbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = StudentDbAdapter.class.getSimpleName();
    private static List<Student> mStudentList = new ArrayList<>();
    private final int ITEM_TYPE_LOADING = 0;
    private final int ITEM_TYPE_STUDENT = 1;

    public StudentDbAdapter(List<Student> list) {
        mStudentList = new ArrayList<>(list);
    }

    public void newDataAdded(List<Student> list) {
        mStudentList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public synchronized void loaded10MoreDownItems(List<Student> list) {
        mStudentList.addAll(new ArrayList<>(list));
        mStudentList = new ArrayList<>(mStudentList.subList(10, mStudentList.size()));
    }

    public synchronized void loaded10MoreUpItems(List<Student> list) {
        mStudentList.addAll(0, list);
        mStudentList = new ArrayList<>(mStudentList.subList(0, 50));
    }

    public void displayLoadingItem(boolean atStart) {
        if (atStart) {
            mStudentList.add(0, null);
        } else {
            mStudentList.add(null);
        }
    }

    public void removeLoadingItem() {
        mStudentList.remove(null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM_TYPE_LOADING:
                Log.d(TAG, "loading item");
                View view = inflater.inflate(R.layout.item_loading_progress, parent, false);
                return new LoadingProgressViewHolder(view);
            case ITEM_TYPE_STUDENT:
                view = inflater.inflate(R.layout.item_student, parent, false);
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
            LoadingProgressViewHolder viewHolder = (LoadingProgressViewHolder) holder;
            viewHolder.setProgressBar();
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


    private static class LoadingProgressViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgressBar;

        private LoadingProgressViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.pb_loading);
        }

        private void setProgressBar() {
            // TODO: Implement progress bar
            mProgressBar.isShown();

        }
    }

    private static class StudentItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mRollNo;
        private TextView mStudentName;

        private StudentItemViewHolder(View itemView) {
            super(itemView);
            mRollNo = itemView.findViewById(R.id.tv_roll_no);
            mStudentName = itemView.findViewById(R.id.tv_student_name);
        }

        private void setStudentData(int position) {
            mRollNo.setText(String.valueOf(mStudentList.get(position).getRollNo()));
            mStudentName.setText(mStudentList.get(position).getName());
        }
    }
}
