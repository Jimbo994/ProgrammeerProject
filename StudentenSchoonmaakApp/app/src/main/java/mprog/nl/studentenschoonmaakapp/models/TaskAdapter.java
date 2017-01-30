package mprog.nl.studentenschoonmaakapp.models;

import android.app.Activity;
import android.view.View;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

/**
 * Created by Jim on 30-1-2017.
 */

public class TaskAdapter extends FirebaseListAdapter<Task>{

    public TaskAdapter(Query ref, int layout, Activity activity){
        super(activity, Task.class, layout, ref);
    }


    @Override
    protected void populateView(View v, Task model, int position) {

    }
}
