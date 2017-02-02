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
import mprog.nl.studentenschoonmaakapp.models.MyAdapter;
import mprog.nl.studentenschoonmaakapp.models.Group;
import mprog.nl.studentenschoonmaakapp.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class MakeGroupActivity extends AppCompatActivity implements View.OnClickListener {

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

        // Initialize onClick Listener on FloatingActionButton
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

        // Edittext in dialog
        mNameField = (EditText) dialog.findViewById(R.id.field_invite_member_name);
        mEmailField = (EditText) dialog.findViewById(R.id.field_invite_member_email);

        // Buttons in dialog
        Button invite = (Button) dialog.findViewById(R.id.invite_member_button);
        Button cancel = (Button) dialog.findViewById(R.id.invite_cancel_button);

        // Inflate dialog
        dialog.show();

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    String email = mEmailField.getText().toString();
                    String name = mNameField.getText().toString();
                    mUser = new User();
                    mUser.setLastName(null);
                    mUser.setName(name);
                    mUser.setEmail(email);
                    mMemberList.add(mUser);
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

    private void setListViewClickListener() {
        // OnItemLongClickListener inflates dialog where member can be edited.
        mMemberListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           final int index, long arg3) {

                final Dialog dialog = new Dialog(MakeGroupActivity.this);
                dialog.setContentView(R.layout.custom_dialog_edit_member);

                // Edittext in AlertDialog.
                mNameField = (EditText) dialog.findViewById(R.id.field_edit_member_name);
                mEmailField = (EditText) dialog.findViewById(R.id.field_edit_member_email);

                // Buttons in Alertdialog.
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
                            String email = mEmailField.getText().toString();
                            String name = mNameField.getText().toString();
                            mUser = new User();
                            mUser.setLastName(null);
                            mUser.setName(name);
                            mUser.setEmail(email);
                            mMemberList.add(mUser);
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
                });
                return true;
            }
        });
    }

    // onClick listeners on ActivityButtons
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.cancel_button) {
            finish();
        } else if (i == R.id.save_button) {
           createNewGroup();
        }
    }

    // Creates a new group and calls all functions that are needed to do this.
    //
    private boolean createNewGroup() {
        if (validateform2()){
            // Usage:
            CurrentUsertoGroup();
            List<Future<?>> members = new ArrayList<>();
            for (User u : mMemberList) {
                members.add(MemberAuthentication(u));
                members.add(MemberRegistration(u));
                members.add(MembertoGroup(u));
                members.add(SendMail(u));
            }
            for (Future<?> member : members) {
                try {
                    member.get(); // await completion
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        finish();
        mMemberList.clear();
        return true;
    }

    private void CurrentUsertoGroup() {
        //Get email of currently signed in user
        String email_current_user = mAuth.getCurrentUser().getEmail();
        Toast.makeText(this, "Current email:" + email_current_user, Toast.LENGTH_SHORT).show();

        //Get hash of currently signed in user
        String hash_email_current_user = String.valueOf(email_current_user.hashCode());

        Toast.makeText(this, "Writing current user with hash:" + hash_email_current_user, Toast.LENGTH_SHORT).show();

        // get timestamp for unique group id
        Long tsLong = System.currentTimeMillis()/1000;
        final String timestamp = tsLong.toString();

        final String group = mGroupnameField.getText().toString();

        // maak unieke groep id
        mGroupId = hash_email_current_user + timestamp;

        //groep toevoegen aan ingelogde user
        DatabaseReference userref2 = FirebaseDatabase.getInstance().getReference().child("users").child(hash_email_current_user).child("groups");
        //String key2 = userref2.push().getKey();

        Group group2 = new Group(group, mGroupId);
        Map<String, Object> postvalues2 = group2.toMap();

        Map<String, Object> childUpdates2 = new HashMap<>();

        childUpdates2.put(mGroupId, postvalues2);
        userref2.updateChildren(childUpdates2);

        //Maak groep aan
        DatabaseReference groupref = FirebaseDatabase.getInstance().getReference().child("groups");
        groupref.child(mGroupId).child("groupname").setValue(group);

        //ingelogde user toevoegen aan groep
        DatabaseReference groupref2 = FirebaseDatabase.getInstance().getReference().child("groups").child(mGroupId);
        String key = groupref2.push().getKey();
        groupref2.child("members").child(key).setValue(hash_email_current_user);

        //naam toevoegen aan members
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

    Future<?> MemberAuthentication(User u) {
        String email = u.getEmail();

        List<Future<?>> mUsersFuture = new ArrayList<>();

        Toast.makeText(MakeGroupActivity.this, "Email =" + email,
                Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(email, "blablabla")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    }

                });
        return new AsyncResult<>();
    }

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



    private Future<?> MembertoGroup(User u) {

        //Get email of currently signed in user
        String email_current_user = mAuth.getCurrentUser().getEmail();

        //Get hash of currently signed in user
        String hash_email_current_user = String.valueOf(email_current_user.hashCode());

        // Get email of user
        String email = u.getEmail();

        // get hash of email
        String hash_email = String.valueOf(email.hashCode());

        // get timestamp for unique group id
        Long tsLong = System.currentTimeMillis()/1000;
        final String timestamp = tsLong.toString();


        final String group = mGroupnameField.getText().toString();

        // maak unieke groep id
//        groupid = hash_email_current_user + timestamp;

        Toast.makeText(this, "Writing user u, but current user hash ="+ hash_email_current_user, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Writing user u hash:" + hash_email, Toast.LENGTH_SHORT).show();

        //groep toevoegen aan user u
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("users").child(hash_email).child("groups");
//        String key = userref.push().getKey();

        Group group1 = new Group(group, mGroupId);
        Map<String, Object> postvalues = group1.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(mGroupId, postvalues);
        userref.updateChildren(childUpdates);

        //members toevoegen aan groep
        DatabaseReference groupref2 = FirebaseDatabase.getInstance().getReference().child("groups").child(mGroupId);
        String key2 = groupref2.push().getKey();
        groupref2.child("members").child(key2).setValue(hash_email);
        groupref2.child("membernames").child(key2).setValue(u.getName());

        return new AsyncResult<>();
    }

    // Sends Password Reset Email to invited users. In Firebase a custom template is created
    // with a invitation text.
    private Future<?> SendMail(User u) {
        String email = u.getEmail();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MakeGroupActivity.this, R.string.email_verzonden,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MakeGroupActivity.this, R.string.iets_misgegaan,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return new AsyncResult<>();
    }


    // Checks if TextFields are properly filled in.
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError(getString(R.string.vulemailin));
            result = false;

        }else if (TextUtils.equals(mEmailField.getText().toString(), mEmail_current_user)){
            mEmailField.setError(getString(R.string.ubentalingelogd));
            result = false;
        }
        else {
            mEmailField.setError(null);
        }
        if (TextUtils.isEmpty(mNameField.getText().toString())) {
            mNameField.setError(getString(R.string.vulbijnaamin));
            result = false;
        } else {
            mNameField.setError(null);
        }
        return result;
    }

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





