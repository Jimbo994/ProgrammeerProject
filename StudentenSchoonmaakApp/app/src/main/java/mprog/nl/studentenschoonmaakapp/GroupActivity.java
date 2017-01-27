package mprog.nl.studentenschoonmaakapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mprog.nl.studentenschoonmaakapp.models.CustomAdapter;
import mprog.nl.studentenschoonmaakapp.models.CustomTaskAdapter;
import mprog.nl.studentenschoonmaakapp.models.FireBaseHelper;

public class GroupActivity extends AppCompatActivity {

    EditText mRoomField;
    EditText mRemoveRoomField;
    String groupid;
    String groupname;

    ArrayList<String> RoomList;
    ArrayList<String> Members;

    DatabaseReference mDatabase;
    DatabaseReference mDatabase_for_members;

//    FireBaseHelper mHelper;
    ArrayAdapter mAdapter;
    ArrayAdapter<String> mSpinnerAdapter;

    Spinner spinner;

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

        mRooms =(ListView) findViewById(R.id.tasks_listview);
        RoomList = new ArrayList<>();
        Members = new ArrayList<>();
        mAdapter = new CustomAdapter(this, RoomList);


        mSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Members);


        Toast.makeText(GroupActivity.this,("groepid: " + groupid + " groepnaam: " + groupname), Toast.LENGTH_LONG).show();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child("rooms");
        mDatabase_for_members = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child("membernames");
//        mHelper = new FireBaseHelper(mDatabase);
//        RoomList = mHelper.retrieve_rooms(RoomList);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RoomList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    RoomList.add(String.valueOf(ds.getValue()));
                }
                mRooms.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                final String[] room_name = {mRooms.getItemAtPosition(i).toString()};
                Toast.makeText(getApplicationContext(), "kamer" + room_name[0], Toast.LENGTH_SHORT).show();

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
                        ref_tasks.child(room_name[0]).removeValue();

                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    String current_room_name = ds.getValue(String.class);
                                    if (current_room_name.equals(room_name[0])){
                                        ds.getRef().removeValue();
                                        room_name[0] = "";

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
                mAdapter.notifyDataSetChanged();
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

        // Spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        mDatabase_for_members.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Members.add(String.valueOf(ds.getValue()));
                }
                spinner.setAdapter(mSpinnerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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



