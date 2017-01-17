package mprog.nl.studentenschoonmaakapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.Attributes;

public class InviteActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MY_PREFS_NAME = "MemberList";
    private static final String TAG = "InviteActivity";

    private ArrayList<String> mMembers;
    private EditText mNameField;
    private EditText mEmailField;

    public String name;

    // [START declare_auth&database]
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    // [END declare_auth&database]

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        getSupportActionBar().setTitle("Deelnemer Toevoegen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        TinyDB tinydb = new TinyDB(context);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        // ArrayList
        mMembers = new ArrayList<String>();

        //Views
        mNameField = (EditText) findViewById(R.id.field_invite_name);
        mEmailField = (EditText) findViewById(R.id.field_invite_email);

        //Buttons
        findViewById(R.id.confirm_invite_button).setOnClickListener(this);
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
            mNameField.setError("Vul e-mail in.");
            result = false;
        } else {
            mEmailField.setError(null);
        }
        return result;
    }

    private void invite() {
        Log.d(TAG, "Inviting...");
        if (!validateForm()) {
            return;
        } else {
            String email = mEmailField.getText().toString();
            String name = mNameField.getText().toString();
            editor.putString("name", name);
            editor.putString("Email", email);
            editor.commit();

            //mMembers.add(name + ", " + email);
            Toast.makeText(this, "Goed gedaan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.confirm_invite_button) {
            invite();
            Intent intent = new Intent(InviteActivity.this, MakeGroupActivity.class);
            //intent.putExtra("MemberList", mMembers);
            startActivity(intent);
            }
        }


    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(InviteActivity.this, MakeGroupActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

}

