/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 */

package mprog.nl.studentenschoonmaakapp.models;

/**
 * This class contains a Task Object. This object contains the information of a Task,
 * consisting of the name of the task, a boolean to check if the task is completed. and a timestamp
 * to check when it was done.
 * It is used to write a Task to FireBase.
 */

public class Task {

    private String task;
    private boolean completed;
    private String timestamp;

    public Task(){
    }

    public Task (String task, boolean completed, String timestamp) {
        this.task = task;
        this.completed = completed;
        this.timestamp = timestamp;
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

    public String getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
}
