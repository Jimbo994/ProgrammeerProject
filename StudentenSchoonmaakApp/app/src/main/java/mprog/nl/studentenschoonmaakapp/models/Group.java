/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 *
 */
package mprog.nl.studentenschoonmaakapp.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jim on 19-1-2017.
 */

@IgnoreExtraProperties
public class Group {
    private static String groupid;
    private static String groupname;

    public Group() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Group(String groupname, String groupid) {
        Group.groupname = groupname;
        Group.groupid = groupid;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("groupid", groupid);
        result.put("groupname", groupname);

        return result;
    }

    public String getGroupid() {
        return Group.groupid;
    }

    public String getGroupname(){
        return Group.groupname;
    }
    // [END post_to_map]

}
// [END post_class]
