package com.example.todo_list;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private Cursor cursor;

    // Priority constants

    public static final int PRIORITY_NOT_IMPORTANT = 0;
    public static final int PRIORITY_LESS_IMPORTANT = 1;
    public static final int PRIORITY_IMPORTANT = 2;
    public static final int PRIORITY_VERY_IMPORTANT = 3;

    public TaskAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }


    // Takes the priority integer as an input and returns the corresponding priority description
    private String getPriorityDescription(int priority) {
        switch (priority) {
            case DbContract.DbToDoListContract.PRIORITY_NOT_IMPORTANT:
                return "Not Important";
            case DbContract.DbToDoListContract.PRIORITY_LESS_IMPORTANT:
                return "Less Important";
            case DbContract.DbToDoListContract.PRIORITY_IMPORTANT:
                return "Important";
            case DbContract.DbToDoListContract.PRIORITY_VERY_IMPORTANT:
                return "Very Important";
            default:
                return "Unknown Priority";
        }
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName;
        public TextView taskPriority;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_description);
            taskPriority = itemView.findViewById(R.id.task_priority);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String taskName = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DbToDoListContract.COLUMN_NAME_TASK));
        long taskId = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.DbToDoListContract._ID));


        int nameColumnIndex = cursor.getColumnIndex(DbContract.DbToDoListContract.COLUMN_NAME_TASK);
        if (nameColumnIndex == -1) {
            throw new IllegalArgumentException("Column '" + DbContract.DbToDoListContract.COLUMN_NAME_TASK + "' doesn't exist in the database.");
        }
        int priorityColumnIndex = cursor.getColumnIndex(DbContract.DbToDoListContract.COLUMN_NAME_PRIORITY);
        if (nameColumnIndex != -1 && priorityColumnIndex != -1) {
            String name = cursor.getString(nameColumnIndex);
            int priority = cursor.getInt(priorityColumnIndex);
            String priorityDescription = getPriorityDescription(priority);  // Convert priority integer to string description

            holder.taskName.setText(name);
            holder.taskPriority.setText(priorityDescription);  // Set the priority description
        } else {
            holder.taskName.setText(R.string.column_not_found);
            holder.taskPriority.setText("N/A");
        }

        holder.itemView.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) v.getContext();
            mainActivity.setSelectedTask(taskId);
            mainActivity.toggleFabMode(false);
        });
    }

    // Swaps the cursor when new data is loaded.
    // This method is called in each fragment
    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
