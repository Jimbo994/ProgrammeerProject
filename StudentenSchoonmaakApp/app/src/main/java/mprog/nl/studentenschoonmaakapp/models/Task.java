package mprog.nl.studentenschoonmaakapp.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jim on 30-1-2017.
 */

public class Task {

    private String task;
    private boolean completed;

    public Task(){

    }
    public Task (String task, boolean completed) {
        this.task = task;
        this.completed = completed;
    }

    public String getTask() {
        return task;
    }

    public boolean isCompleted(){
        return completed;
    }

    public void setTask(String task){
        this.task = task;
    }

    public void setCompleted(boolean completed){
        this.completed = completed;
    }
}
