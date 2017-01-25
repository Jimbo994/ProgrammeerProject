package mprog.nl.studentenschoonmaakapp;

import android.app.Dialog;
import android.content.Intent;
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

import mprog.nl.studentenschoonmaakapp.models.FireBaseHelper;

public class GroupActivity extends AppCompatActivity {

    EditText mRoomField;
    EditText mRemoveRoomField;
    String groupid;
    String groupname;

    DatabaseReference mDatabase;

    FireBaseHelper mHelper;
    ArrayAdapter mAdapter;

    ListView mRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kamers");
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

        Toast.makeText(GroupActivity.this,("groepid: " + groupid + " groepnaam: " + groupname), Toast.LENGTH_LONG).show();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child("rooms");

        mRooms =(ListView) findViewById(R.id.tasks_listview);

        mHelper = new FireBaseHelper(mDatabase);

        if (mHelper.retrieve_tasks() != null)
        {
            mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mHelper.retrieve_rooms());
            mRooms.setAdapter(mAdapter);
        }

        mRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent groupdetail = new Intent (getApplicationContext(), GroupDetailActivity.class);
                groupdetail.putExtra("kamer", mRooms.getItemAtPosition(i).toString());
                groupdetail.putExtra("ref", mDatabase.toString());
                groupdetail.putExtra("groepid", groupid);
                groupdetail.putExtra("groepnaam", groupname);

                startActivity(groupdetail);
            }
        });

        mRooms.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String room_name = mRooms.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), "kamer" + room_name, Toast.LENGTH_SHORT).show();

                final Dialog dialog = new Dialog(GroupActivity.this);
                dialog.setContentView(R.layout.custom_dialog_remove_room);

                // Edittext in AlertDialog
                mRemoveRoomField = (EditText) dialog.findViewById(R.id.field_add_task);

                // Buttons in Alertdialog
                Button remove = (Button) dialog.findViewById(R.id.remove_button);
                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);

                dialog.show();

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference ref_tasks = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child("tasks");
                        ref_tasks.child(room_name).removeValue();

                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    String current_room_name = ds.getValue(String.class);
                                    if (current_room_name.equals(room_name)){
                                        ds.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });
    }

    private void AddTask() {

        final Dialog dialog = new Dialog(GroupActivity.this);
        dialog.setContentView(R.layout.custom_dialog_add_room);

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
                    DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid);
                    String key = db_ref.push().getKey();

                    db_ref.child("rooms").child(key).setValue(room);
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



