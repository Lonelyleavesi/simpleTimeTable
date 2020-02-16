package com.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.activity.R;
import com.project.fragment.SelectCourseTimeDialogFrament;
import com.project.item.CourseTime;

import java.util.List;

public class SetCourseTimeAdapter extends RecyclerView.Adapter<SetCourseTimeAdapter.ViewHolder> {

    private List<CourseTime>  courseTimeList;
    private Context context;

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
        final FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        holder.buttonCourseStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCourseTimeDialogFrament courseTimeDialogFrament = new SelectCourseTimeDialogFrament();
                courseTimeDialogFrament.show(manager,"");
            }
        });
        holder.buttonCourseEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCourseTimeDialogFrament courseTimeDialogFrament = new SelectCourseTimeDialogFrament();
                courseTimeDialogFrament.show(manager,"");
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseTime courseTime = courseTimeList.get(position);
        holder.tvCourseNo.setText("第"+courseTime.getNo()+"节课");
        holder.buttonCourseStartTime.setText(courseTime.getStart_hour()+":"+courseTime.getStart_minute());
        holder.buttonCourseEndTime.setText(courseTime.getEnd_hour()+":"+courseTime.getEnd_minute());
    }

    @Override
    public int getItemCount() {
        return courseTimeList.size();
    }

}
