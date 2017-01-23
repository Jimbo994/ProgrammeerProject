package mprog.nl.studentenschoonmaakapp.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jim on 19-1-2017.
 */

// [START post_class]
@IgnoreExtraProperties
public class Post {
    private static String groupid;
    private static String groupname;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String groupname, String groupid) {
        Post.groupname = groupname;
        Post.groupid = groupid;
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
        return Post.groupid;
    }

    public String getGroupname(){
        return Post.groupname;
    }
    // [END post_to_map]

}
// [END post_class]
