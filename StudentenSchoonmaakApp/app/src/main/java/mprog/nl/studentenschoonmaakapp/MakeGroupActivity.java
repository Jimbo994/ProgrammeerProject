package mprog.nl.studentenschoonmaakapp;

import android.app.Dialog;
import android.os.AsyncTask;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import mprog.nl.studentenschoonmaakapp.models.MyAdapter;
import mprog.nl.studentenschoonmaakapp.models.User;

public class MakeGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MakeGroupActivity";

    // [START declare_auth&database]
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    // [END declare_auth&database]

    MyAdapter arrayAdapter;
    ArrayList<User> mMemberList;
    User user;
    String name;

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

        // ArrayList
        //mMemberList = getIntent().getStringArrayListExtra("MemberList");

        mMemberList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

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
                            user.setLastName(null);
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
                            user.setLastName(null);
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
            finish();
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Vul e-mail in.");
            result = false;
        } else {
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

    private void writeNewGroup() {
        if (TextUtils.isEmpty(mGroupnameField.getText().toString())) {
            mGroupnameField.setError("Vul groepsnaam in.");
        }
        else {
            Toast.makeText(this, "Goed gedaan", Toast.LENGTH_SHORT).show();
            // Usage:
            List<Future<?>> members = new ArrayList<>();
            for (User u : mMemberList) {
                members.add(MemberRegister(u));
            }
            for (Future<?> member : members){
                try {
                    member.get(); // await completion
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void WriteGroup() {
        Toast.makeText(MakeGroupActivity.this, "Nu nog de groep schrijven en de leden erin zetten",
                Toast.LENGTH_SHORT).show();
    }

    Future<?> MemberRegister(User u) {
            Log.d(TAG, "signIn:onComplete:" + u.getEmail());

            String email = u.getEmail();
            name = u.getName();

            Toast.makeText(MakeGroupActivity.this, "Email =" + email,
                    Toast.LENGTH_SHORT).show();

             mAuth.createUserWithEmailAndPassword(email, "blablabla")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Toast.makeText(MakeGroupActivity.this, "Auth gelukt",
                                Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            List<Future<?>> users= new ArrayList<>();
                                users.add(onAuthSuccess(task.getResult().getUser()));
                            for (Future<?> user : users){
                                try {
                                    user.get(); // await completion
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            Toast.makeText(MakeGroupActivity.this, "Registreren",
                                    Toast.LENGTH_SHORT).show();
                        }
                        }else {
                            Toast.makeText(MakeGroupActivity.this, "Registreren mislukt.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return new AsyncResult<>();
        }

    Future<?> onAuthSuccess(FirebaseUser user) {

        // Write new user
        //writeNewUser(user.getUid(), name, "undefined", user.getEmail());
        String uid = user.getUid();
        String email = user.getEmail();

        User user2 = new User(name, "undefined", email);
        mDatabase.child("users").child(uid).setValue(user2);

        assert email != null;
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

        // [START basic_write]
        //public void writeNewUser(String uid, String name, String lastname, String email) {
            //User user = new User(name, lastname, email);

           // mDatabase.child("users").child(uid).setValue(user);
        //}
        // [END basic_write]
}



