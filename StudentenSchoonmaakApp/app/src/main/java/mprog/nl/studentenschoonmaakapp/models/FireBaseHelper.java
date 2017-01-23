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

    public FireBaseHelper(DatabaseReference db) {
        this.db = db;
        mygroups = new ArrayList<>();
    }

    /** Retrieves Arraylist, on Eventlisteners fetchdata is called which fills Arraylist
     Not all Eventlisteners used yet.*/
    public ArrayList<String> retrieve() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
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
           post = ds.getValue(Post.class);
           String groupname = post.getGroupname();
           mygroups.add(groupname);
        }
    }
}

