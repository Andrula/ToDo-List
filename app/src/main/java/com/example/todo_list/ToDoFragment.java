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

public class ToDoFragment extends Fragment {

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
        adapter = new TaskAdapter(getContext(), getToDoTasks());
        recyclerView.setAdapter(adapter);

        return view;
    }

    // When fragment is visible to user, refreshTaskList method is called and swaps old cursor with new one
    // and then refreshes the RecyclerView
    @Override
    public void onResume() {
        super.onResume();
        updateDataAndView();
    }

    public void updateDataAndView() {
        Cursor newCursor = getToDoTasks();
        adapter.swapCursor(newCursor);
        adapter.notifyDataSetChanged();
    }

    private Cursor getToDoTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DbContract.DbToDoListContract.COLUMN_NAME_STATUS + " = ?";
        String[] selectionArgs = { Integer.toString(DbContract.DbToDoListContract.STATUS_TODO) };

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
}
