package mprog.nl.studentenschoonmaakapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mprog.nl.studentenschoonmaakapp.models.CustomAdapter;
import mprog.nl.studentenschoonmaakapp.models.CustomTaskAdapter;
import mprog.nl.studentenschoonmaakapp.models.FireBaseHelper;
import mprog.nl.studentenschoonmaakapp.models.Task;

public class GroupDetailActivity extends AppCompatActivity {

    String groupid;
    String groupname;
    String ref;
    String room;

    ArrayAdapter mAdapter;
    ArrayList<String> mTaskList;
    ListView mTasks;

    EditText mEditField;
    EditText mTaskField;

//    FireBaseHelper mHelper;
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
        mAdapter = new CustomTaskAdapter(this, mTaskList);

        String key = mDatabase.getKey();

        mAdapter2 = new FirebaseListAdapter<Task>(this, Task.class, R.layout.custom_listview_tasks, mDatabase) {
            @Override
            protected void populateView(View v, final Task model, int position) {
                TextView theTextView = (TextView) v.findViewById(R.id.movie_title);
                CheckBox checkbox = (CheckBox) v.findViewById(R.id.checkBox);

                theTextView.setText(model.getTask());
                checkbox.setChecked(model.isCompleted());

            }
        };

        mTasks.setAdapter(mAdapter2);


//        mHelper = new FireBaseHelper(mDatabase);
//        mHelper.retrieve_tasks(mTaskList);

//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mTaskList.clear();
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    Task task = ds.getValue(Task.class);
//                    String t = task.getTask();
////                    String string = (String.valueOf(ds.getValue()));
////                    String[] split = string.split("=");
////                    split[0] = split[0].substring(1);
//                   mTaskList.add(t);
//                }
//                mTasks.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        mTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String task_name = mTasks.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), "taak" + task_name, Toast.LENGTH_SHORT).show();

                final Dialog dialog = new Dialog(GroupDetailActivity.this);

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
                            RemoveTask(ref_tasks, task_name);

                            String key = ref_tasks.push().getKey();

                            ref_tasks.child(key).setValue(new_task);
                            dialog.dismiss();
                        }
                    }
                });

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference ref_tasks = FirebaseDatabase.getInstance().getReference().child("groups").
                                child(groupid).child("tasks").child(room);

                        RemoveTask(ref_tasks, task_name);
                        dialog.dismiss();
                    }

                });
                return true;
            }
        });
    }

    private void RemoveTask(DatabaseReference ref, final String string) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String current_task_name = ds.getValue(String.class);
                    if (current_task_name.equals(string)) {
                        ds.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void AddTask() {
                final Dialog dialog = new Dialog(GroupDetailActivity.this);
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
                            String task = mTaskField.getText().toString();
                            Task new_task = new Task();
                            new_task.setTask(task);
                            new_task.setCompleted(false);

                            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child("tasks").child(room);

                            String key = db_ref.push().getKey();

                            db_ref.child(task).setValue(new_task);

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
            Task t = new Task(task_string, true);
            ref.setValue(t);
            Toast.makeText(this, "checked", Toast.LENGTH_SHORT).show();
        }
        else{
            Task t = new Task(task_string, false);
            ref.setValue(t);
        }
    }
}

