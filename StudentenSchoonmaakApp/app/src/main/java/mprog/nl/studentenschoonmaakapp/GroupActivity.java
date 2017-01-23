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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mprog.nl.studentenschoonmaakapp.models.FireBaseHelper;


    EditText mRoomField;
    String groupid;
    String groupname;

    DatabaseReference mDatabase;

    FireBaseHelper mHelper;

    ArrayAdapter mAdapter;


    ListView mTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Taken");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTask();
            }
        });

        groupid = getIntent().getStringExtra("groepid");
        groupname = getIntent().getStringExtra("groepnaam");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child(groupname);

        mTasks =(ListView) findViewById(R.id.tasks_listview);

        mHelper = new FireBaseHelper(mDatabase);

        if (mHelper.retrieve_tasks() != null)
        {
            mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mHelper.retrieve_tasks());
            mTasks.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        mTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        mTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

    }

    private void AddTask() {

        dialog.setContentView(R.layout.custom_dialog_add_task);

        // Edittext in AlertDialog
        mRoomField = (EditText) dialog.findViewById(R.id.field_add_room);

        // Buttons in Alertdialog
        Button save = (Button) dialog.findViewById(R.id.add_room_button);
        Button cancel = (Button) dialog.findViewById(R.id.cancel_add_room_button);

        dialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                } else {
                    String room = mRoomField.getText().toString();
                    DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child(groupname);
                    String key = db_ref.push().getKey();

                    db_ref.child("tasks").child(key).setValue(room);
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
        if (TextUtils.isEmpty(mRoomField.getText().toString())) {
            mRoomField.setError("Vul een kamer in.");
            result = false;
        }
        else{
            mRoomField.setError(null);
        }
        return result;
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}



