package com.example.todo_list;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ToDoDbHelper dbHelper;
    private FloatingActionButton fabAdd, fabDelete;
    private long selectedTaskId = -1;

    private boolean isAddMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ToDoDbHelper(this);
        setupViewPager();

        fabAdd = findViewById(R.id.AddTask);
        fabDelete = findViewById(R.id.DeleteTask);

        fabAdd.setOnClickListener(view -> {
            if (isAddMode) {
                showAddTaskDialog();
            } else {
                completeTask();
                toggleFabMode(true);
            }
        });

        fabDelete.setOnClickListener(view -> {
            if (selectedTaskId != -1) {
                deleteTask();  // Call deleteTask if a task is selected
                toggleFabMode(true);  // Reset FAB to add mode
            } else {
                Toast.makeText(MainActivity.this, "No task selected!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupViewPager() {
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(getTabTitle(position))
        ).attach();
    }
    private String getTabTitle(int position) {
        return position == 0 ? "All Tasks" : position == 1 ? "To Do" : "Completed";
    }

    public void toggleFabMode(boolean resetToAddMode) {
        isAddMode = resetToAddMode;

        fabDelete.setVisibility(isAddMode ? View.GONE : View.VISIBLE);
        fabAdd.setImageResource(isAddMode ? R.drawable.add24 : R.drawable.done);
        if (!isAddMode) {
            fabAdd.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_green_light));
        } else {
            fabAdd.setColorFilter(null);
        }
    }
    private void showAddTaskDialog() {
        DialogFragment dialog = new AddTaskDialogFragment();
        dialog.show(getSupportFragmentManager(), "AddTaskDialogFragment");
    }

    public void setSelectedTask(long id) {
        selectedTaskId = id;
        toggleFabMode(false);
    }
    private void completeTask() {
        if (selectedTaskId == -1) {
            Toast.makeText(this, "No task selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_STATUS, DbContract.DbToDoListContract.STATUS_COMPLETED);
        String selection = DbContract.DbToDoListContract._ID + " = ?";
        String[] selectionArgs = { String.valueOf(selectedTaskId) };

        int count = dbHelper.getWritableDatabase().update(DbContract.DbToDoListContract.TABLE_NAME, values, selection, selectionArgs);
        if (count > 0) {
            Toast.makeText(this, "Task completed successfully!", Toast.LENGTH_SHORT).show();
            updateFragments();
        } else {
            Toast.makeText(this, "Failed to complete task.", Toast.LENGTH_SHORT).show();
        }
        toggleFabMode(true); // Reset to add mode
    }

    private void deleteTask() {
        if (selectedTaskId == -1) {
            Toast.makeText(this, "No task selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        String selection = DbContract.DbToDoListContract._ID + " = ?";
        String[] selectionArgs = { String.valueOf(selectedTaskId) };
        int count = dbHelper.getWritableDatabase().delete(DbContract.DbToDoListContract.TABLE_NAME, selection, selectionArgs);
        if (count > 0) {
            Toast.makeText(this, "Task deleted successfully!", Toast.LENGTH_SHORT).show();
            updateFragments();
        } else {
            Toast.makeText(this, "Failed to delete task.", Toast.LENGTH_SHORT).show();
        }
        toggleFabMode(true); // Reset to add mode
    }

    // Method used to clear all data
    public void clearAllTasks() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbContract.DbToDoListContract.TABLE_NAME, null, null);
    }

    // Method used to add data and test visibility of task in application.
    private void insertTestData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DbContract.DbToDoListContract.COLUMN_NAME_TASK, "Buy groceries");
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_STATUS, DbContract.DbToDoListContract.STATUS_TODO);
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_PRIORITY, DbContract.DbToDoListContract.PRIORITY_IMPORTANT);
        db.insert(DbContract.DbToDoListContract.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_TASK, "Read a book");
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_STATUS, DbContract.DbToDoListContract.STATUS_COMPLETED);
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_PRIORITY, DbContract.DbToDoListContract.PRIORITY_LESS_IMPORTANT);
        db.insert(DbContract.DbToDoListContract.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_TASK, "Workout");
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_STATUS, DbContract.DbToDoListContract.STATUS_COMPLETED);
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_PRIORITY, DbContract.DbToDoListContract.PRIORITY_NOT_IMPORTANT);
        db.insert(DbContract.DbToDoListContract.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_TASK, "Take a shower");
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_STATUS, DbContract.DbToDoListContract.STATUS_COMPLETED);
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_PRIORITY, DbContract.DbToDoListContract.PRIORITY_VERY_IMPORTANT);
        db.insert(DbContract.DbToDoListContract.TABLE_NAME, null, values);
    }

    public void addTaskAndUpdateViews(String taskName, int priority) {
        ToDoDbHelper dbHelper = new ToDoDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_TASK, taskName);
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_STATUS, DbContract.DbToDoListContract.STATUS_TODO); // Default status
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_PRIORITY, priority);
        long newRowId = db.insert(DbContract.DbToDoListContract.TABLE_NAME, null, values);

        if (newRowId != -1) {
            updateFragments();
        }
    }

    private void updateFragments() {
        // Refresh specific fragments if they are visible or need updating
        Fragment allTasksFragment = getSupportFragmentManager().findFragmentByTag("all_tasks_fragment");
        if (allTasksFragment instanceof AllTasksFragment) {
            ((AllTasksFragment) allTasksFragment).updateDataAndView();
        }

        Fragment toDoFragment = getSupportFragmentManager().findFragmentByTag("todo_fragment");
        if (toDoFragment instanceof ToDoFragment) {
            ((ToDoFragment) toDoFragment).updateDataAndView();
        }

        Fragment completedTasksFragment = getSupportFragmentManager().findFragmentByTag("completed_tasks_fragment");
        if (completedTasksFragment instanceof CompletedTasksFragment) {
            ((CompletedTasksFragment) completedTasksFragment).updateDataAndView();
        }
    }

    public void markTaskAsComplete() {
        FloatingActionButton fabAdd = findViewById(R.id.AddTask);
        FloatingActionButton fabDelete = findViewById(R.id.DeleteTask);

        if (fabDelete.getVisibility() == View.GONE) {
            fabDelete.setVisibility(View.VISIBLE);
            fabAdd.setImageResource(R.drawable.done);
            fabAdd.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_green_light));
        } else {
            fabDelete.setVisibility(View.GONE);
            fabAdd.setImageResource(R.drawable.add24);
            fabAdd.setColorFilter(null);
        }
    }
}
