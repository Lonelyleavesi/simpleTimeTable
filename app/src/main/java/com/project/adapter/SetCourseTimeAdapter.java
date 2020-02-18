package com.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.activity.R;
import com.project.fragment.SelectCourseTimeDialogFragment;
import com.project.item.CourseTime;
import com.project.tools.CustomTime;

import java.util.List;

/**
 * @author  chen yujie
 */
public class SetCourseTimeAdapter extends RecyclerView.Adapter<SetCourseTimeAdapter.ViewHolder> {

    private List<CourseTime>  courseTimeList;
    private Context context;
    private FragmentManager manager;
    private final int TIME_OF_ONE_COURSE = 45;
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvCourseNo;
        Button  buttonCourseStartTime;
        Button  buttonCourseEndTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseNo = (TextView) itemView.findViewById(R.id.textView_setCourseNo);
            buttonCourseStartTime = (Button) itemView.findViewById(R.id.button_setCourseStartTime);
            buttonCourseEndTime = (Button) itemView.findViewById(R.id.button_setCourseEndTime);
        }
    }

    public SetCourseTimeAdapter(List<CourseTime> t_courseTimeList,Context context){
        this.courseTimeList = t_courseTimeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_class_time,parent,false);
        ViewHolder holder = new ViewHolder(view);
        manager = ((AppCompatActivity)context).getSupportFragmentManager();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CourseTime courseTime = courseTimeList.get(position);
        holder.tvCourseNo.setText("第"+courseTime.getNo()+"节课");
        holder.buttonCourseStartTime.setText(courseTime.start_time.formatTime());
        holder.buttonCourseEndTime.setText(courseTime.end_time.formatTime());
        holder.buttonCourseStartTime.setOnClickListener(new View.OnClickListener() {
            /**
             * 先将设置前的时间传入到dialog之中，点击确定后再将dialog中设置的时间传出来
             */
            @Override
            public void onClick(View v) {
                final SelectCourseTimeDialogFragment courseTimeDialogFragment = new SelectCourseTimeDialogFragment();
                courseTimeDialogFragment.m_time = courseTime.start_time;
                courseTimeDialogFragment.setDialogClickListener(new SelectCourseTimeDialogFragment.SelectTimeClickListener() {
                    @Override
                    public void onSureClick() {
                        courseTime.start_time = courseTimeDialogFragment.m_time;
                        holder.buttonCourseStartTime.setText(courseTime.start_time.formatTime());
                        courseTime.end_time = new CustomTime(courseTime.start_time);
                        courseTime.end_time.addMinute(TIME_OF_ONE_COURSE);
                        holder.buttonCourseEndTime.setText(courseTime.end_time.formatTime());

                    }
                });
                courseTimeDialogFragment.show(manager,"");
            }
        });
        holder.buttonCourseEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SelectCourseTimeDialogFragment courseTimeDialogFragment = new SelectCourseTimeDialogFragment();
                courseTimeDialogFragment.m_time = courseTime.end_time;
                courseTimeDialogFragment.setDialogClickListener(new SelectCourseTimeDialogFragment.SelectTimeClickListener() {
                    @Override
                    public void onSureClick() {
                        courseTime.end_time = courseTimeDialogFragment.m_time;
                        holder.buttonCourseEndTime.setText(courseTime.end_time.formatTime());
                    }
                });
                courseTimeDialogFragment.show(manager,"");
            }
        });
    }


    @Override
    public int getItemCount() {
        return courseTimeList.size();
    }

}
