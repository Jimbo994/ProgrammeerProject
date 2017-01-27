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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mprog.nl.studentenschoonmaakapp.models.CustomAdapter;
import mprog.nl.studentenschoonmaakapp.models.FireBaseHelper;

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
        mAdapter = new CustomAdapter(this, mTaskList);


//        mHelper = new FireBaseHelper(mDatabase);
//        mHelper.retrieve_tasks(mTaskList);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTaskList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    mTaskList.add(String.valueOf(ds.getValue()));
                }
                mTasks.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
                            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child("tasks").child(room);

                            String key = db_ref.push().getKey();

                            db_ref.child(key).setValue(task);
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
        }

