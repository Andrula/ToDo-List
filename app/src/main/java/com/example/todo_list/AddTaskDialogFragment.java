package com.example.todo_list;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddTaskDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_task, null);

        final EditText editTextTaskName = view.findViewById(R.id.edittext_task_name);
        final Spinner spinnerPriority = view.findViewById(R.id.spinner_priority);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String taskName = editTextTaskName.getText().toString();
                        int priority = spinnerPriority.getSelectedItemPosition();
                        ((MainActivity) getActivity()).addTaskAndUpdateViews(taskName, priority);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        return builder.create();
    }
}

