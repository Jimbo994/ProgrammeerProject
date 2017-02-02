/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 */

package mprog.nl.studentenschoonmaakapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * This class contains a Group Object. This object contains the information of a Group,
 * consisting of the name of the group and its Id.
 * It is used to write a Group to FireBase.
 */

@IgnoreExtraProperties
public class Group {
    private static String groupId;
    private static String groupName;

    public Group() {
    }

    public Group(String groupName, String groupId) {
        Group.groupName = groupName;
        Group.groupId = groupId;
    }

    public String getGroupId() {
        return Group.groupId;
    }

    public String getGroupName(){
        return Group.groupName;
    }
}

