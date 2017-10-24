package com.example.amit.timesaver;

/**
 * Created by amit on 24/10/17.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder>{



        private List<Task> tasksList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView taskSubject, taskDescription;
            public CheckBox taskStatus;

            public MyViewHolder(View view) {
                super(view);
                taskSubject = (TextView) view.findViewById(R.id.task_subject);
                taskDescription = (TextView) view.findViewById(R.id.task_description);
                taskStatus = (CheckBox) view.findViewById(R.id.task_checkbox_done);
            }

        }

    public TasksAdapter(List<Task> tasksList) {
        this.tasksList = tasksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task task = tasksList.get(position);
        holder.taskSubject.setText(task.getRelatedCourse().getName());
        holder.taskDescription.setText(task.getDescription());
        holder.taskStatus.setChecked(task.isDone());
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }
}
