package mprog.nl.studentenschoonmaakapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mprog.nl.studentenschoonmaakapp.models.FireBaseHelper;

public class GroupDetailActivity extends AppCompatActivity {

    String groupid;
    String groupname;
    String ref;
    String room;

    ArrayAdapter mAdapter;

    ListView mTasks;

    EditText mTaskField;

    FireBaseHelper mHelper;

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

        mDatabase = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child(groupname).child("tasks").child(room);

        mTasks =(ListView) findViewById(R.id.tasks_listview);

        mHelper = new FireBaseHelper(mDatabase);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mHelper.retrieve_tasks());
        mTasks.setAdapter(mAdapter);

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
                if (!validateForm()) {
                    return;
                } else {
                    String task = mTaskField.getText().toString();
                    DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child(groupname).child("tasks").child(room);
                    db_ref.getKey();
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

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mTaskField.getText().toString())) {
            mTaskField.setError("Vul een kamer in.");
            result = false;
        }
        else{
            mTaskField.setError(null);
        }
        return result;
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
