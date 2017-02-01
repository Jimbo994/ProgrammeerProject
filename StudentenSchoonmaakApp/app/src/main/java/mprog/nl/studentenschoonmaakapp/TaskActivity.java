/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 */

package mprog.nl.studentenschoonmaakapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mprog.nl.studentenschoonmaakapp.models.Task;

import java.util.Date;

/**
 * Shows ListView with all tasks of a room. These tasks can be
 * edited and deleted on longItemClick. The tasks can also be checked to mark for completion.
 * Via a floating action button a task can be added.
 */

public class TaskActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private ListView mTasks;
    private EditText mEditField;
    private EditText mTaskField;

    String groupId;
    String groupName;
    String ref;
    String room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize and set FloatingActionButton.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTask();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Getting data from intent.
        groupId = getIntent().getStringExtra("groepid");
        groupName = getIntent().getStringExtra("groepnaam");
        ref = getIntent().getStringExtra("ref");
        room = getIntent().getStringExtra("kamer");

        // Initialize DatabaseReference.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).child("tasks").child(room);

        setListView();
        setListViewClickListener();
    }

    // Initializes and fills ListView.
    private void setListView() {

        mTasks = (ListView) findViewById(R.id.tasks_listview);

        // initialize FirebaseListAdapters that retrieves and shows Task object form mDatabase.
        FirebaseListAdapter<Task> mAdapter = new FirebaseListAdapter<Task>(this, Task.class, R.layout.custom_listview_tasks, mDatabase) {
            @Override
            protected void populateView(View v, final Task model, int position) {
                TextView Task = (TextView) v.findViewById(R.id.movie_title);
                TextView date = (TextView) v.findViewById(R.id.date);
                CheckBox checkbox = (CheckBox) v.findViewById(R.id.checkBox);

                Task.setText(model.getTask());
                checkbox.setChecked(model.isCompleted());
                date.setText(model.getTimestamp());
            }
        };

        mTasks.setAdapter(mAdapter);
    }

    // Sets onItemLongClickListener on ListView.
    private void setListViewClickListener() {

        mTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task = (Task) mTasks.getItemAtPosition(i);
                final String task_name = task.getTask();

                final Dialog dialog = new Dialog(TaskActivity.this);
                dialog.setContentView(R.layout.custom_dialog_edit_tasks);

                // Edittext in dialog.
                mEditField = (EditText) dialog.findViewById(R.id.field_edit);

                // Buttons in dialog.
                Button edit = (Button) dialog.findViewById(R.id.edit_button);
                Button remove = (Button) dialog.findViewById(R.id.remove_button);

                // DataBaseReference.
                final DatabaseReference ref_tasks = FirebaseDatabase.getInstance().getReference().child("groups")
                        .child(groupId).child("tasks").child(room);
                dialog.show();

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeTask(task_name);
                        dialog.dismiss();
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (validateForm(mEditField)) {
                            // Remove old Task.
                            removeTask(task_name);
                            // Create new Task and write to DataBase.
                            Task edittask = new Task(mEditField.getText().toString(), false, "");
                            ref_tasks.child(mEditField.getText().toString()).setValue(edittask);
                            dialog.dismiss();
                        }
                    }
                });
                return true;
            }
        });
    }

    // Removes task from DataBase.
    private void removeTask(String task_name) {
        DatabaseReference ref_tasks = FirebaseDatabase.getInstance().getReference().child("groups").
                child(groupId).child("tasks").child(room);
        ref_tasks.child(task_name).removeValue();
    }

    // Inflates dialog where a task can be created and added.
    private void AddTask() {
        // Initialize dialog and set View.
        final Dialog dialog = new Dialog(TaskActivity.this);
        dialog.setContentView(R.layout.custom_dialog_add_task);

        // Edittext in AlertDialog.
        mTaskField = (EditText) dialog.findViewById(R.id.field_add_task);

        // Buttons in AlertDialog.
        Button save = (Button) dialog.findViewById(R.id.add_task_button);
        Button cancel = (Button) dialog.findViewById(R.id.cancel_add_task_button);

        // Inflate dialog.
        dialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm(mTaskField)) {
                    saveTask();
                    dialog.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    // Writes task to database.
    private void saveTask() {
        Task new_task = new Task();
        new_task.setTask(mTaskField.getText().toString());
        new_task.setCompleted(false);

        DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference().child("groups").
                child(groupId).child("tasks").child(room);
        db_ref.child(mTaskField.getText().toString()).setValue(new_task);
    }

    // Checks if TextFields are properly filled in.
    private boolean validateForm(EditText edittext) {
        boolean result = true;
        if (TextUtils.isEmpty(edittext.getText().toString())) {
            edittext.setError(getString(R.string.vultaakin));
            result = false;
        } else {
            edittext.setError(null);
        }
        return result;
    }

    // OnClick listener for checkboxes in ListView. Checkboxes can be checked/unchecked
    // to mark completion of Task. A date is added at the time of completion.
    public void onClick(View view) {
        // Get parent View and Data.
        View parent = (View) view.getParent();
        TextView task = (TextView) parent.findViewById(R.id.movie_title);

        DatabaseReference ref = mDatabase.child(task.getText().toString());

        // If checked mark completion as true by writing Task to Database,
        // Else mark completion as false, and set no Date.
        boolean isChecked = ((CheckBox)view).isChecked();
        if (isChecked){
            String str_timestamp = new Date().toString();
            Task t = new Task(task.getText().toString(), true, str_timestamp);
            ref.setValue(t);
        }
        else{
            Task t = new Task(task.getText().toString(), false, "");
            ref.setValue(t);
        }
    }

    // Finishes activity on navigate back button click.
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

