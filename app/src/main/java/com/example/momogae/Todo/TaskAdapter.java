package com.example.momogae.Todo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.momogae.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {


    private static final String DATE_FORMAT = "dd/MM/yyy";
    final private ItemClickListener mItemClickListener;
    final private CheckBoxCheckListener mCheckBoxCheckListener;
    private List<TaskEntity> mTaskEntries;
    private Context mContext;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public TaskAdapter(Context context, ItemClickListener listener, CheckBoxCheckListener mCheckBoxCheckListener) {
        mContext = context;
        mItemClickListener = listener;
        this.mCheckBoxCheckListener = mCheckBoxCheckListener;
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_layout, parent, false);

        return new TaskViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        TaskEntity taskEntity = mTaskEntries.get(position);
        String description = taskEntity.getDescription();
        String updatedAt = dateFormat.format(taskEntity.getUpdatedAt());

        holder.taskDescriptionView.setText(description); //할일
        holder.updatedAtView.setText(updatedAt); //날짜
        holder.checkBox.setChecked(taskEntity.isChecked()); //체크박스

        if(taskEntity.isChecked()){ //체크박스 체크시 할일 글자 변경 이벤트
            holder.taskDescriptionView.setBackgroundResource(R.drawable.strike_through);
            holder.taskDescriptionView.setTextColor(Color.GRAY);

        }else {
            holder.taskDescriptionView.setBackgroundResource(0);
            holder.taskDescriptionView.setTextColor(ContextCompat.getColor(mContext, R.color.list_item_text_color));

        }
    }

    @Override
    public int getItemCount() {
        if (mTaskEntries == null) {
            return 0;
        }
        return mTaskEntries.size();
    }

    public List<TaskEntity> getTasks() {
        return mTaskEntries;
    }



    public void setTasks(List<TaskEntity> taskEntries) {
        mTaskEntries = taskEntries;
        notifyDataSetChanged();
    }


    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    public interface CheckBoxCheckListener {
        void onCheckBoxCheckListener(TaskEntity taskEntity);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { //뷰홀더 클릭이벤트 발생시


        TextView taskDescriptionView;
        TextView updatedAtView;
        CheckBox checkBox;


        public TaskViewHolder(View itemView) {
            super(itemView);

            taskDescriptionView = itemView.findViewById(R.id.taskDescription);
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(this);

            checkBox.setOnClickListener(new View.OnClickListener() { //체크박스 선택/비선택 홀더 모습
                @Override
                public void onClick(View v) {
                    if(mTaskEntries.get(getAdapterPosition()).isChecked()){
                        mTaskEntries.get(getAdapterPosition()).setChecked(false);
                    }else {
                        mTaskEntries.get(getAdapterPosition()).setChecked(true);
                    }
                    mCheckBoxCheckListener.onCheckBoxCheckListener(mTaskEntries.get(getAdapterPosition()));
                }
            });

        }

        @Override
        public void onClick(View view) {
            int elementId = mTaskEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }

    }
}