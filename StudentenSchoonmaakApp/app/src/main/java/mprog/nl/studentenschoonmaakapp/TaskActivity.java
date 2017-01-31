/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 *
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import mprog.nl.studentenschoonmaakapp.models.Task;

public class TaskActivity extends AppCompatActivity {

    String groupid;
    String groupname;
    String ref;
    String room;

    private ArrayList<String> mTaskList;
    ListView mTasks;

    EditText mEditField;
    EditText mTaskField;

    FirebaseListAdapter<Task> mAdapter2;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTask();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getting data from intent
        groupid = getIntent().getStringExtra("groepid");
        groupname = getIntent().getStringExtra("groepnaam");
        ref = getIntent().getStringExtra("ref");
        room = getIntent().getStringExtra("kamer");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child("tasks").child(room);

        mTasks = (ListView) findViewById(R.id.tasks_listview);
        mTaskList = new ArrayList<>();


        mAdapter2 = new FirebaseListAdapter<Task>(this, Task.class, R.layout.custom_listview_tasks, mDatabase) {
            @Override
            protected void populateView(View v, final Task model, int position) {
                TextView theTextView = (TextView) v.findViewById(R.id.movie_title);
                TextView date = (TextView) v.findViewById(R.id.date);
                CheckBox checkbox = (CheckBox) v.findViewById(R.id.checkBox);


                theTextView.setText(model.getTask());
                checkbox.setChecked(model.isCompleted());
                date.setText(model.getTimestamp());

            }
        };

        mTasks.setAdapter(mAdapter2);

        mTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task = (Task) mTasks.getItemAtPosition(i);
                final String task_name = task.getTask();
                Toast.makeText(getApplicationContext(), "taak" + task_name, Toast.LENGTH_SHORT).show();

                final Dialog dialog = new Dialog(TaskActivity.this);

                dialog.setContentView(R.layout.custom_dialog_edit_tasks);

                dialog.show();

                // Edittext in AlertDialog
                mEditField = (EditText) dialog.findViewById(R.id.field_edit);

                // Buttons in Alertdialog
                Button edit = (Button) dialog.findViewById(R.id.edit_button);
                Button remove = (Button) dialog.findViewById(R.id.remove_button);

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!validateForm(mEditField)) {
                            return;
                        } else {
                            String new_task = mEditField.getText().toString();
                            DatabaseReference ref_tasks = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child("tasks").child(room);
                            Task edittask = new Task(new_task, false, "");

                            ref_tasks.child(task_name).removeValue();

                            ref_tasks.child(new_task).setValue(edittask);
                            dialog.dismiss();
                        }
                    }
                });

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference ref_tasks = FirebaseDatabase.getInstance().getReference().child("groups").
                                child(groupid).child("tasks").child(room);
                        ref_tasks.child(task_name).removeValue();
                        dialog.dismiss();
                    }

                });
                return true;
            }
        });
    }


    private void AddTask() {
                final Dialog dialog = new Dialog(TaskActivity.this);
                dialog.setContentView(R.layout.custom_dialog_add_task);

                // Edittext in AlertDialog
                mTaskField = (EditText) dialog.findViewById(R.id.field_add_task);

                // Buttons in Alertdialog
                Button save = (Button) dialog.findViewById(R.id.add_task_button);
                Button cancel = (Button) dialog.findViewById(R.id.cancel_add_task_button);

                dialog.show();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!validateForm(mTaskField)) {
                            return;
                        } else {
                            Task new_task = new Task();
                            new_task.setTask(mTaskField.getText().toString());
                            new_task.setCompleted(false);

                            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child("tasks").child(room);

                            db_ref.child(mTaskField.getText().toString()).setValue(new_task);

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

            private boolean validateForm(EditText edittext) {
                boolean result = true;
                if (TextUtils.isEmpty(edittext.getText().toString())) {
                    edittext.setError("Vul een kamer in.");
                    result = false;
                } else {
                    edittext.setError(null);
                }
                return result;
            }

            public boolean onSupportNavigateUp() {
                finish();
                return true;
            }

    public void onClick(View view) {
        View parent = (View) view.getParent();
        TextView task = (TextView) parent.findViewById(R.id.movie_title);
        String task_string = String.valueOf(task.getText().toString());
        Toast.makeText(this, task_string, Toast.LENGTH_SHORT).show();
        DatabaseReference ref = mDatabase.child(task_string);


        boolean isChecked = ((CheckBox)view).isChecked();
        if (isChecked){
            String str_timestamp = new Date().toString();
            Task t = new Task(task_string, true, str_timestamp);
            ref.setValue(t);
            Toast.makeText(this, "checked", Toast.LENGTH_SHORT).show();
        }
        else{
            Task t = new Task(task_string, false, "");
            ref.setValue(t);
        }
    }
}
