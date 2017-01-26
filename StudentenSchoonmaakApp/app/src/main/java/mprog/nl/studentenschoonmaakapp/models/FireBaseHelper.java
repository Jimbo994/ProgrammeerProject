package mprog.nl.studentenschoonmaakapp.models;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mprog.nl.studentenschoonmaakapp.MyAccountActivity;

/**
 * Created by Jim on 19-1-2017.
 */

public class FireBaseHelper {

    DatabaseReference db;
    Post post;
    ArrayList<String> mygroupids;
    public FireBaseHelper(DatabaseReference db) {
        this.db = db;
        mygroupids = new ArrayList<>();

    }

    /** Retrieves Arraylist, on Eventlisteners fetchdata is called which fills Arraylist
     Not all Eventlisteners used yet.*/
    public ArrayList<String> retrieve(final ArrayList<String> a) {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                a.clear();
                fetchData(a, dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return a;
    }

    public ArrayList retrieve_groupid(final ArrayList<String> a) {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(a, dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mygroupids;
    }


    /** fetches data from database and adds it to Arraylist*/
    private void fetchData(ArrayList<String> a, DataSnapshot dataSnapshot)
    {
        a.clear();
       for (DataSnapshot ds : dataSnapshot.getChildren()) {
           post = ds.getValue(Post.class);
           String groupname = post.getGroupname();
           String groupid = post.getGroupid();
           mygroupids.add(groupid);
           a.add(groupname);
        }
    }


    public ArrayList<String> retrieve_rooms(final ArrayList<String> a) {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                a.clear();
                fetchAllRooms(a, dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return a;
    }

    private void fetchAllRooms(ArrayList<String> a, DataSnapshot dataSnapshot){
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            a.add(String.valueOf(ds.getValue()));
        }
    }

    public ArrayList<String> retrieve_tasks(final ArrayList<String> a) {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                a.clear();
                fetchAllTasks(a, dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return a;
    }

    private void fetchAllTasks(ArrayList<String> a, DataSnapshot dataSnapshot){
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            a.add(String.valueOf(ds.getValue()));
        }
    }
}

