/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 */

package mprog.nl.studentenschoonmaakapp.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("groupid", groupId);
        result.put("groupname", groupName);

        return result;
    }

    public String getGroupid() {
        return Group.groupId;
    }

    public String getGroupname(){
        return Group.groupName;
    }
}

