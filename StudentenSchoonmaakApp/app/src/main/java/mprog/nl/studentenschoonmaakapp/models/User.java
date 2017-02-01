/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 */

package mprog.nl.studentenschoonmaakapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * This class contains a User Object. This object contains the information of a user,
 * consisting of a email, name and lastname. It is used to write user information to FireBase.
 */

@IgnoreExtraProperties
public class User {

    private String email;
    private String name;
    private String lastName;

    public User() {
    }

    public User(String name, String lastName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public  String getLastName() {
        return lastName;
    }

    public void setLastName(String lastname){
        this.lastName = lastname;
    }
}
