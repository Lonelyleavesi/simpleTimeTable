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

import java.util.List;

public class SetCourseTimeAdapter extends RecyclerView.Adapter<SetCourseTimeAdapter.ViewHolder> {

    private List<CourseTime>  courseTimeList;
    private Context context;
    private FragmentManager manager;
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
        holder.buttonCourseStartTime.setText(formatTime(courseTime.getStart_hour())+":"+formatTime(courseTime.getStart_minute()));
        holder.buttonCourseEndTime.setText(formatTime(courseTime.getEnd_hour())+":"+formatTime(courseTime.getEnd_minute()));

        holder.buttonCourseStartTime.setOnClickListener(new View.OnClickListener() {
            /**
             * 先将设置前的时间传入到dialog之中，点击确定后再将dialog中设置的时间传出来
             */
            @Override
            public void onClick(View v) {
                final SelectCourseTimeDialogFragment courseTimeDialogFragment = new SelectCourseTimeDialogFragment();
                courseTimeDialogFragment.m_hour = courseTime.getStart_hour();
                courseTimeDialogFragment.m_hour = courseTime.getStart_hour();
                courseTimeDialogFragment.setDialogClickListener(new SelectCourseTimeDialogFragment.SelectTimeClickListener() {
                    @Override
                    public void onSureClick() {
                        courseTime.setStart_hour(courseTimeDialogFragment.m_hour);
                        courseTime.setStart_minute(courseTimeDialogFragment.m_minute);
                        holder.buttonCourseStartTime.setText(formatTime(courseTime.getStart_hour())+":"+formatTime(courseTime.getStart_minute()));
                    }
                });
                courseTimeDialogFragment.show(manager,"");
            }
        });
        holder.buttonCourseEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SelectCourseTimeDialogFragment courseTimeDialogFragment = new SelectCourseTimeDialogFragment();
                courseTimeDialogFragment.setDialogClickListener(new SelectCourseTimeDialogFragment.SelectTimeClickListener() {
                    @Override
                    public void onSureClick() {
                        courseTime.setEnd_hour(courseTimeDialogFragment.m_hour);
                        courseTime.setEnd_minute(courseTimeDialogFragment.m_minute);
                        holder.buttonCourseEndTime.setText(formatTime(courseTime.getEnd_hour())+":"+formatTime(courseTime.getEnd_minute()));
                    }
                });
                courseTimeDialogFragment.show(manager,"");
            }
        });
    }

    /**
     * 如果时间小于10 比如 5 ，则改成05
     * @param n  输入的数字
     * @return   返回字符串
     */
    private String formatTime(Integer n){
        String re = n.toString();
        if (n < 10){
            re = "0"+re;
        }
        return re;
    }
    @Override
    public int getItemCount() {
        return courseTimeList.size();
    }

}
