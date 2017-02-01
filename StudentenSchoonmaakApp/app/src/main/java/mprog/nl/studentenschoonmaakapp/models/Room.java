/**
 * Created by Jim on 30-1-2017.
 */


package mprog.nl.studentenschoonmaakapp.models;

/**
 * This class contains a Room Object. This object contains the information of a Room,
 * consisting of the name of the room and the name of the user responsible for cleaning this
 * room.
 * It is used to write a Room to FireBase.
 */

public class Room {

    private String room;
    private String responsibility;

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
