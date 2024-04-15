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

public class AllTasksFragment extends Fragment {

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
        adapter = new TaskAdapter(getContext(), getAllTasks());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private Cursor getAllTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
                DbContract.DbToDoListContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DbContract.DbToDoListContract.COLUMN_NAME_PRIORITY  // Order by priority
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDataAndView();
    }

    public void updateDataAndView() {
        Cursor newCursor = getAllTasks();
        adapter.swapCursor(newCursor);
        adapter.notifyDataSetChanged();
    }
}

