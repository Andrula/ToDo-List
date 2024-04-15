package com.example.todo_list;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CompletedTasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private ToDoDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.rvTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new ToDoDbHelper(getContext());
        adapter = new TaskAdapter(getContext(), getCompletedTasks());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private Cursor getCompletedTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DbContract.DbToDoListContract.COLUMN_NAME_STATUS + " = ?";
        String[] selectionArgs = { Integer.toString(DbContract.DbToDoListContract.STATUS_COMPLETED) };

        return db.query(
                DbContract.DbToDoListContract.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                DbContract.DbToDoListContract.COLUMN_NAME_PRIORITY // Order by priority
        );
    }

    // When fragment is visible to user, refreshTaskList method is called and swaps old cursor with new one
    // and then refreshes the RecyclerView
    @Override
    public void onResume() {
        super.onResume();
        updateDataAndView();
    }

    public void updateDataAndView() {
        Cursor newCursor = getCompletedTasks();  // Fetch new data for completed tasks
        adapter.swapCursor(newCursor);           // Swap the old cursor with the new one
        adapter.notifyDataSetChanged();          // Refresh the RecyclerView to display new data
    }
}
