/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 */
package mprog.nl.studentenschoonmaakapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mprog.nl.studentenschoonmaakapp.models.AsyncResult;
import mprog.nl.studentenschoonmaakapp.models.BaseActivity;
import mprog.nl.studentenschoonmaakapp.models.MyAdapter;
import mprog.nl.studentenschoonmaakapp.models.Group;
import mprog.nl.studentenschoonmaakapp.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * In this class a user can make a group by giving it a name and adding members.
 * Members can be added by giving them a name and an email in a dialog. The member is then visbble
 * in a ListView where onLongItemClick the member can be editted or removed. Once the user is done
 * with adding members and naming the group. The user can save the group.
 * On saving a group* this class register every user invited,
 * writes them to FireBase database and sends them a invitation email.
 * Also the group is written on FireBase. When a newly invited user sets up their password by
 * clicking on a link in their invitation email they can log in and have direct access to the group.
 */

public class MakeGroupActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<User> mMemberList;

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mGroupnameField;

    ListView mMemberListView;

    private FirebaseAuth mAuth;

    private MyAdapter mArrayAdapter;

    private String mGroupId;
    private String mEmail_current_user;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Views.
        mGroupnameField = (EditText) findViewById(R.id.field_make_group_name);
        mMemberListView = (ListView) findViewById(R.id.MemberList);

        // Buttons.
        findViewById(R.id.cancel_button).setOnClickListener(this);
        findViewById(R.id.save_button).setOnClickListener(this);

        // Initialize ArrayList.
        mMemberList = new ArrayList<>();
        // make sure it is empty.
        mMemberList.clear();

        // Initialize Auth and get current user.
        mAuth = FirebaseAuth.getInstance();
        mEmail_current_user = mAuth.getCurrentUser().getEmail();

        // Initialize ArrayAdapter.
        mArrayAdapter = new MyAdapter(this, mMemberList);

        // set Adapter.
        if (mMemberList != null) {
            mMemberListView.setAdapter(mArrayAdapter);
        }

        // Initialize onClick Listener on FloatingActionButton.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.invite_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMember();
            }
        });
        setListViewClickListener();
    }

    // Inflates a Dialog in which a member can be added to mMemberList ArrayList.
    private void addMember() {
        final Dialog dialog = new Dialog(MakeGroupActivity.this);
        dialog.setContentView(R.layout.custom_dialog_invite);

        // EditText in dialog.
        mNameField = (EditText) dialog.findViewById(R.id.field_invite_member_name);
        mEmailField = (EditText) dialog.findViewById(R.id.field_invite_member_email);

        // Buttons in dialog.
        Button invite = (Button) dialog.findViewById(R.id.invite_member_button);
        Button cancel = (Button) dialog.findViewById(R.id.invite_cancel_button);

        // Inflate dialog.
        dialog.show();

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    setUser();
                    mArrayAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    // Retrieves data from EditText and puts it into User object and adds it to arraylist.
    private void setUser() {
        String email = mEmailField.getText().toString();
        String name = mNameField.getText().toString();
        mUser = new User();
        mUser.setLastName(null);
        mUser.setName(name);
        mUser.setEmail(email);
        mMemberList.add(mUser);
    }

    private void setListViewClickListener() {
        // OnItemLongClickListener inflates dialog where member can be edited.
        mMemberListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           final int index, long arg3) {
                final Dialog dialog = new Dialog(MakeGroupActivity.this);
                dialog.setContentView(R.layout.custom_dialog_edit_member);
                // EditText in dialog.
                mNameField = (EditText) dialog.findViewById(R.id.field_edit_member_name);
                mEmailField = (EditText) dialog.findViewById(R.id.field_edit_member_email);
                // Buttons in dialog.
                Button edit = (Button) dialog.findViewById(R.id.edit_member_button);
                Button remove = (Button) dialog.findViewById(R.id.remove_member_button);
                //inflate dialog.
                dialog.show();
                // Edit clicked member in ArrayList.
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (validateForm()) {
                            mMemberList.remove(index);
                            setUser();
                            mArrayAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });
                // Removes clicked member from ArrayList.
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMemberList.remove(index);
                        mArrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }); return true;
            }
        });
    }

    // onClick listeners on ActivityButtons.
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.cancel_button) {
            finish();
        } else if (i == R.id.save_button) {
           createNewGroup();
        }
    }

    // Creates a new group and calls all functions that are needed to do this in the right order by
    // using a Future List. By using a Future it is made sure that activity only finishes until
    // for loop is finished.
        private boolean createNewGroup() {
        if (validateform2()){

            showProgressDialog();

            CurrentUserToGroup();
            // Make List Future
            List<Future<?>> members = new ArrayList<>();
            for (User u : mMemberList) {
                members.add(MemberAuthentication(u));
                members.add(MemberRegistration(u));
                members.add(MembertoGroup(u));
                members.add(SendMail(u));
            }
            for (Future<?> member : members) {
                try {
                    // await completion, only completes once all members are retrieved.
                    member.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        hideProgressDialog();
        finish();
        mMemberList.clear();

        return true;
    }

    // Creates a new group under current user and under groups.
    private void CurrentUserToGroup() {
        //Get email and hash of currently signed in user.
        String email_current_user = mAuth.getCurrentUser().getEmail();
        String hash_email_current_user = String.valueOf(email_current_user.hashCode());

        // get timestamp for unique group id.
        Long tsLong = System.currentTimeMillis()/1000;
        final String timestamp = tsLong.toString();

        // create unique groupId.
        mGroupId = hash_email_current_user + timestamp;

        // Retrieve groupName.
        final String group = mGroupnameField.getText().toString();

        createGroupUnderUsers(hash_email_current_user, group);
        createGroupunderGroups(hash_email_current_user, group);
    }


    private void createGroupUnderUsers(String hash_email_current_user, String group) {
        // DatabaseReference.
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("users").child(hash_email_current_user).child("groups");

        // Create new Group object and write to DataBase.
        Group group2 = new Group(group, mGroupId);
        userref.child(mGroupId).setValue(group2);
    }

    private void createGroupunderGroups(String hash_email_current_user, String group) {
        // Create group under groups.
        DatabaseReference groupref = FirebaseDatabase.getInstance().getReference().child("groups");
        groupref.child(mGroupId).child("groupname").setValue(group);

        // add current user to members in groups.
        DatabaseReference groupref2 = FirebaseDatabase.getInstance().getReference().child("groups").child(mGroupId);
        String key = groupref2.push().getKey();
        groupref2.child("members").child(key).setValue(hash_email_current_user);

        // add current username to membernames in groups.
        final String[] current_name = new String[1];
        final DatabaseReference members = FirebaseDatabase.getInstance().getReference().child("groups").child(mGroupId).child("membernames");
        DatabaseReference me = FirebaseDatabase.getInstance().getReference().child("users").child(hash_email_current_user).child("userinfo");
        me.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    current_name[0] = String.valueOf(ds.getValue());
                }
                members.push().setValue(current_name[0]);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Writes a user in FireBase DataBase with hash of email as userId.
    private Future<?> MemberRegistration(User u) {

        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("users");
        String email = u.getEmail();
        String name = u.getName();
        User user = new User(name, "", email);

        int hash = email.hashCode();
        String hash_email = String.valueOf(hash);

        userref.child(hash_email).child("userinfo").setValue(user);
        return new AsyncResult<>();
    }

    // Registrates a user in FireBase Auth.
    Future<?> MemberAuthentication(User u) {
        String email = u.getEmail();

        mAuth.createUserWithEmailAndPassword(email, "blablabla")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    }

                });
        return new AsyncResult<>();
    }

    private Future<?> MembertoGroup(User u) {

        // Get email of user.
        String email = u.getEmail();
        // get hash of email.
        String hash_email = String.valueOf(email.hashCode());

        final String group = mGroupnameField.getText().toString();

        createGroupUnderUsers(hash_email, group);

        // Add members to group.
        DatabaseReference groupref2 = FirebaseDatabase.getInstance().getReference().child("groups").child(mGroupId);
        String key = groupref2.push().getKey();
        groupref2.child("members").child(key).setValue(hash_email);
        groupref2.child("membernames").child(key).setValue(u.getName());
        return new AsyncResult<>();
    }

    // Sends Password Reset Email to invited users. In FireBase a custom template is created
    // with a invitation text.
    private Future<?> SendMail(User u) {
        String email = u.getEmail();
        mAuth.sendPasswordResetEmail(email);
        return new AsyncResult<>();
    }

    // Checks if TextFields are properly filled in.
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError(getString(R.string.vulemailin));
            result = false;
        } else if (TextUtils.equals(mEmailField.getText().toString(), mEmail_current_user)){
            mEmailField.setError(getString(R.string.ubentalingelogd));
            result = false;
        } else {
            mEmailField.setError(null);
        } if (TextUtils.isEmpty(mNameField.getText().toString())) {
            mNameField.setError(getString(R.string.vulbijnaamin));
            result = false;
        } else {
            mNameField.setError(null);
        }
        return result;
    }

    // Checks if TextFields are properly filled in.
    private boolean validateform2() {
        boolean result = true;
        if (TextUtils.isEmpty(mGroupnameField.getText().toString())) {
            mGroupnameField.setError(getString(R.string.vulgroepsnaamin));
            result =  false;
        } else if ((mMemberList.size() == 0)) {
            Toast.makeText(this, R.string.voegledentoe, Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }
}





