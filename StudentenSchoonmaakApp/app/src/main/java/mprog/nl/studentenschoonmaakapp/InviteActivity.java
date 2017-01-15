package mprog.nl.studentenschoonmaakapp;

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

public class InviteActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "InviteActivity";

    private EditText mNameField;
    private EditText mEmailField;

    public String name;
    public String email;

    // [START declare_auth&database]
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    // [END declare_auth&database]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        getSupportActionBar().setTitle("Deelnemer Toevoegen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

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
            Toast.makeText(this, "Goed gedaan", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.confirm_invite_button) {
            invite();
            }
        }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}

