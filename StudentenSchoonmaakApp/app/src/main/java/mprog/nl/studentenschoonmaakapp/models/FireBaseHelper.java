package mprog.nl.studentenschoonmaakapp.models;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Jim on 19-1-2017.
 */

public class FireBaseHelper {

    DatabaseReference db;
    ArrayList<String> mygroups;

    public FireBaseHelper(DatabaseReference db) {
        this.db = db;
        mygroups = new ArrayList<>();
    }

    /** Retrieves Arraylist, on Eventlisteners fetchdata is called which fills Arraylist
     Not all Eventlisteners used yet.*/
    public ArrayList<String> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mygroups.clear();
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mygroups.clear();
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mygroups;
    }


    /** fetches data from database and adds it to Arraylist*/
    private void fetchData(DataSnapshot dataSnapshot)
    {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            mygroups.add(String.valueOf(ds.getValue()));
        }
    }
}

