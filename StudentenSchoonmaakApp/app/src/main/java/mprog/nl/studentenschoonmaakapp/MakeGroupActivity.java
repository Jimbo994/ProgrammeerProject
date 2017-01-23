package mprog.nl.studentenschoonmaakapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import mprog.nl.studentenschoonmaakapp.models.AsyncResult;
import mprog.nl.studentenschoonmaakapp.models.MyAdapter;
import mprog.nl.studentenschoonmaakapp.models.Post;
import mprog.nl.studentenschoonmaakapp.models.User;

public class MakeGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MakeGroupActivity";

    // [START declare_auth&database]
    private FirebaseAuth mAuth;
    // [END declare_auth&database]

    MyAdapter arrayAdapter;
    ArrayList<User> mMemberList;
    User user;
    String name;

    String groupid;

    String email_current_user;

    List<Future<?>> users;

    EditText mNameField;
    EditText mEmailField;
    EditText mGroupnameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Views
        mGroupnameField = (EditText) findViewById(R.id.field_make_group_name);

        // Buttons
        findViewById(R.id.cancel_button).setOnClickListener(this);
        findViewById(R.id.save_button).setOnClickListener(this);

        // ListView
        final ListView MemberList = (ListView) findViewById(R.id.MemberList);

        mMemberList = new ArrayList<>();

        // make sure it is empty
        mMemberList.clear();

        mAuth = FirebaseAuth.getInstance();
        email_current_user = mAuth.getCurrentUser().getEmail();

        //ArrayAdapter
        //final ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, mMemberList);
        arrayAdapter = new MyAdapter(this, mMemberList);

        if (mMemberList != null){
            MemberList.setAdapter(arrayAdapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.invite_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MakeGroupActivity.this);
                dialog.setContentView(R.layout.custom_dialog_invite);

                // Edittext in AlertDialog
                mNameField = (EditText) dialog.findViewById(R.id.field_invite_member_name);
                mEmailField = (EditText) dialog.findViewById(R.id.field_invite_member_email);

                // Buttons in Alertdialog
                Button invite = (Button) dialog.findViewById(R.id.invite_member_button);
                Button cancel = (Button) dialog.findViewById(R.id.invite_cancel_button);

                dialog.show();

                invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!validateForm()) {
                            return;
                        } else {
                            String email = mEmailField.getText().toString();
                            String name = mNameField.getText().toString();
                            user = new User();
                            user.setLastname(null);
                            user.setName(name);
                            user.setEmail(email);
                            mMemberList.add(user);

                            arrayAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(MakeGroupActivity.this, "Goed gedaan", Toast.LENGTH_SHORT).show();
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
        });

        MemberList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           final int index, long arg3) {

                final Dialog dialog = new Dialog(MakeGroupActivity.this);
                dialog.setContentView(R.layout.custom_dialog_edit);

                // Edittext in AlertDialog
                mNameField = (EditText) dialog.findViewById(R.id.field_edit_member_name);
                mEmailField = (EditText) dialog.findViewById(R.id.field_edit_member_email);

                // Buttons in Alertdialog
                Button edit = (Button) dialog.findViewById(R.id.edit_member_button);
                Button remove = (Button) dialog.findViewById(R.id.remove_member_button);

                dialog.show();

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!validateForm()) {
                            return;
                        } else {
                            mMemberList.remove(index);
                            String email = mEmailField.getText().toString();
                            String name = mNameField.getText().toString();
                            user = new User();
                            user.setLastname(null);
                            user.setName(name);
                            user.setEmail(email);
                            mMemberList.add(user);
                            arrayAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(MakeGroupActivity.this, "Goed gedaan", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMemberList.remove(index);
                        arrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.cancel_button) {
            finish();
        } else if (i == R.id.save_button) {
            writeNewGroup();
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Vul e-mail in.");
            result = false;

        }else if (TextUtils.equals(mEmailField.getText().toString(), email_current_user)){
            mEmailField.setError("U bent al ingelogd met deze email");
            result = false;
        }
        else {
            mEmailField.setError(null);
        }
        if (TextUtils.isEmpty(mNameField.getText().toString())) {
            mNameField.setError("Vul bijnaam in.");
            result = false;
        } else {
            mEmailField.setError(null);
        }
        return result;
    }

    private boolean writeNewGroup() {
        if (TextUtils.isEmpty(mGroupnameField.getText().toString())) {
            mGroupnameField.setError("Vul groepsnaam in.");
            return false;
        }
        else if ((mMemberList.size() == 0)) {
            Toast.makeText(this, "U moet eerst leden toevoegen.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            Toast.makeText(this, "Goed gedaan", Toast.LENGTH_SHORT).show();
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        finish();
        mMemberList.clear();
        return true;
    }

    Future<?> MemberAuthentication(User u) {
        Log.d(TAG, "signIn:onComplete:" + u.getEmail());
        String email = u.getEmail();
        name = u.getName();

        users = new ArrayList<>();

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

        userref.child(hash_email).setValue(user);
        return new AsyncResult<>();
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
        groupid = hash_email_current_user + timestamp;

        //groep toevoegen aan ingelogde user
        DatabaseReference userref2 = FirebaseDatabase.getInstance().getReference().child("users").child(hash_email_current_user).child("groups");
        String key2 = userref2.push().getKey();

        Post post2 = new Post(group, groupid);
        Map<String, Object> postvalues2 = post2.toMap();

        Map<String, Object> childUpdates2 = new HashMap<>();

        childUpdates2.put(key2, postvalues2);
        userref2.updateChildren(childUpdates2);

        //Maak groep aan
        DatabaseReference groupref = FirebaseDatabase.getInstance().getReference().child("groups");
        groupref.child(groupid).setValue(group);

        //ingelogde user toevoegen aan groep
        DatabaseReference groupref2 = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child(group);
        String key = groupref2.getKey();
        groupref2.child("members").child(key).setValue(hash_email_current_user);
        
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
        String key = userref.push().getKey();

        Post post = new Post(group, groupid);
        Map<String, Object> postvalues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(key, postvalues);
        userref.updateChildren(childUpdates);

        //members toevoegen aan groep
        DatabaseReference groupref2 = FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child(group);
        String key2 = groupref2.getKey();
            groupref2.child("members").child(key2).setValue(hash_email);

        return new AsyncResult<>();
    }



    private Future<?> SendMail(User u) {
        String email = u.getEmail();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MakeGroupActivity.this, "E-mail verzonden.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Email sent.");
                        } else {
                            Toast.makeText(MakeGroupActivity.this, "Er is iets misgegaan.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return new AsyncResult<>();
    }
}





