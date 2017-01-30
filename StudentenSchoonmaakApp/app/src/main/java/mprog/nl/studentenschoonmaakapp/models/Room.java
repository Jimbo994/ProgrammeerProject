package mprog.nl.studentenschoonmaakapp.models;

/**
 * Created by Jim on 30-1-2017.
 */

public class Room {

    String room;
    String responsibility;

    public Room(){

    }

    public Room(String room, String responsibility){
        this.room = room;
        this.responsibility = responsibility;
    }

    public String getRoom(){
        return room;
    }

    public void setRoom(String room){
        this.room = room;
    }

    public String getResponsibility(){
        return responsibility;
    }

    public void setResponsibility(String responsibility){
        this.responsibility = responsibility;
    }
}
