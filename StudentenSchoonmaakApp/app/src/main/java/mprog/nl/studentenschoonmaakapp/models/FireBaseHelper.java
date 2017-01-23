package mprog.nl.studentenschoonmaakapp.models;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Jim on 19-1-2017.
 */

public class FireBaseHelper {

    DatabaseReference db;
    ArrayList<String> mygroups;
    Post post;
    ArrayList<String> mygroupids;
    ArrayList<String> myTasks;

    public FireBaseHelper(DatabaseReference db) {
        this.db = db;
        mygroups = new ArrayList<>();
        mygroupids = new ArrayList<>();
        myTasks = new ArrayList<>();

    }

    /** Retrieves Arraylist, on Eventlisteners fetchdata is called which fills Arraylist
     Not all Eventlisteners used yet.*/
    public ArrayList<String> retrieve() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mygroups.clear();
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mygroups;
    }

    public ArrayList retrieve_groupid() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mygroupids;
    }


    /** fetches data from database and adds it to Arraylist*/
    private void fetchData(DataSnapshot dataSnapshot)
    {
        mygroups.clear();
       for (DataSnapshot ds : dataSnapshot.getChildren()) {
           post = ds.getValue(Post.class);
           String groupname = post.getGroupname();
           String groupid = post.getGroupid();
           mygroupids.add(groupid);
           mygroups.add(groupname);
        }
    }


    public ArrayList<String> retrieve_tasks(){
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchAllChildren(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchAllChildren(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                fetchAllChildren(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                fetchAllChildren(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return myTasks;
    }



    private void fetchAllChildren(DataSnapshot dataSnapshot){
        myTasks.clear();
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            myTasks.add(String.valueOf(ds.getValue()));
        }
    }
}

