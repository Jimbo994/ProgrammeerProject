/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 */

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mprog.nl.studentenschoonmaakapp.models.Room;

/**
 * Shows ListView with all rooms of a group in FireBase Database. These rooms can be
 * deleted on longItemClick and contents of the room can be seen onItemClick.
 * Via a floating action button a room can be added.
 */

public class RoomActivity extends AppCompatActivity {

    private ArrayAdapter<String> mMemberAdapter;
    private ArrayList<String> mMembers;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase_for_members;
    private EditText mRoomField;
    private FirebaseListAdapter<Room> mAdapter;
    String groupId;
    String groupName;
    ListView mRooms;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize and set FloatingActionbutton, AddTask() onClick.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoom();
            }
        });

        // Receiving data from Intent.
        groupId = getIntent().getStringExtra("groepid");
        groupName = getIntent().getStringExtra("groepnaam");

        // Initialize Views.
        mRooms =(ListView) findViewById(R.id.rooms_listview);
        mMembers = new ArrayList<>();
        mMemberAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mMembers);

        // Initialize DatabaseReference.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).child("rooms");
        mDatabase_for_members = FirebaseDatabase.getInstance().getReference().child("groups").
                child(groupId).child("membernames");

        setListView();
        setListViewOnItemClickListener();
        setListViewOnLongItemClickListener();
    }

    private void setListView() {
        // FireBaseListAdapter that loads rooms of group.
        mAdapter = new FirebaseListAdapter<Room>(this, Room.class, R.layout.custom_listview_room, mDatabase) {
            @Override
            protected void populateView(View v, Room model, int position) {
                TextView room = (TextView) v.findViewById(R.id.movie_title);
                TextView responsible = (TextView) v.findViewById(R.id.responsible);

                room.setText(model.getRoom());
                responsible.setText(String.format(getString(R.string.verantwoordelijkph), model.getResponsibility()));
            }
        };

        // setAdapter on Listview.
        mRooms.setAdapter(mAdapter);
    }

    private void setListViewOnItemClickListener() {
        // OnItemClickListener starts TaskActivity and sends through room, DatabaseReference,
        // groupId and groupName.
        mRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent groupdetail = new Intent (getApplicationContext(), TaskActivity.class);
                Room room = (Room) mRooms.getItemAtPosition(i);
                groupdetail.putExtra("kamer", room.getRoom());
                groupdetail.putExtra("ref", mDatabase.toString());
                groupdetail.putExtra("groepid", groupId);
                groupdetail.putExtra("groepnaam", groupName);
                startActivity(groupdetail);
            }
        });


    }

    private void setListViewOnLongItemClickListener() {
        // OnItemLongClickListener inflates dialog where user can delete rooms.
        mRooms.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Room room = (Room) mRooms.getItemAtPosition(i);
                final String room_name = room.getRoom();

                // Initialize dialog and set View.
                final Dialog dialog = new Dialog(RoomActivity.this);
                dialog.setContentView(R.layout.custom_dialog_remove_room);

                // Buttons in dialog.
                Button remove = (Button) dialog.findViewById(R.id.remove_button);
                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);
                // Inflate dialog.
                dialog.show();

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeRoom(room_name);
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

    //  Removes a room from database.
    private void removeRoom(final String room_name) {
        // Remove room under tasks.
        DatabaseReference ref_tasks = FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).child("tasks");
        ref_tasks.child(room_name).removeValue();

        // Remove room under rooms.
        mDatabase.child(room_name).removeValue();
    }

    // writes a room to database.
    private void addRoom() {
        // Initialize dialog and set View.
        final Dialog dialog = new Dialog(RoomActivity.this);
        dialog.setContentView(R.layout.custom_dialog_add_room);
        // EditText in dialog.
        mRoomField = (EditText) dialog.findViewById(R.id.field_add_room);
        // Buttons in dialog.
        Button save = (Button) dialog.findViewById(R.id.add_room_button);
        Button cancel = (Button) dialog.findViewById(R.id.cancel_add_room_button);
        // Spinner.
        mSpinner = (Spinner) dialog.findViewById(R.id.member_spinner);

        fillSpinner();

        // Inflate dialog.
        dialog.show();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRoom();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    // Fills Spinner with data from Firebase.
    private void fillSpinner() {
        // Retrieve group members from database to fill Spinner with, then setAdapter.
        mDatabase_for_members.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMembers.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    mMembers.add(String.valueOf(ds.getValue()));
                }
                mSpinner.setAdapter(mMemberAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    // Writes a new room to database.
    public void saveRoom(){
        if (validateForm()) {
            // Retrieve data from EditText and Spinner.
            String room = mRoomField.getText().toString();
            String responsible = mSpinner.getSelectedItem().toString();

            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference().child("groups").child(groupId);

            // Create new Room object and write to database.
            Room new_room = new Room(room, responsible);
            db_ref.child("rooms").child(room).setValue(new_room);
        }
    }

    // Checks if TextFields are properly filled in.
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mRoomField.getText().toString())) {
            mRoomField.setError(getString(R.string.vulkamerin));
            result = false;
        } if (mRoomField.getText().toString().contains(".")){
            mRoomField.setError(getString(R.string.kamermaggeen));
            result = false;
        } else {
            mRoomField.setError(null);
        }
        return result;
    }



    // Finishes activity on navigate back button click.
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}



