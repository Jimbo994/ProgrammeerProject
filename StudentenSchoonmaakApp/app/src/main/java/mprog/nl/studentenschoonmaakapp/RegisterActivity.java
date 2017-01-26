package mprog.nl.studentenschoonmaakapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mprog.nl.studentenschoonmaakapp.models.User;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private EditText mEmailField;
    private EditText mNameField;
    private EditText mLastNameField;
    private EditText mPasswordField;
    // private EditText mConfirmPassword;

    public String name;
    public String lastname;

    // [START declare_auth&database]
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    // [END declare_auth&database]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Registreren");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Views
        mEmailField = (EditText) findViewById(R.id.field_register_email);
        mNameField = (EditText) findViewById(R.id.field_register_name);
        mLastNameField = (EditText) findViewById(R.id.field_register_lastname);
        mPasswordField = (EditText) findViewById(R.id.field_register_password);
        // mConfirmPassword = (EditText) findViewById(R.id.field_register_passwordconfirm);

        //Buttons
        findViewById(R.id.register_button).setOnClickListener(this);
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        String email = mEmailField.getText().toString();
        name = mNameField.getText().toString();
        lastname = mLastNameField.getText().toString();
        String password = mPasswordField.getText().toString();

        //dit nog niet doen, onnodig lastig
        //String passwordconfirm = mConfirmPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                            Toast.makeText(RegisterActivity.this, "Registreren",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registreren mislukt.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {

        // Write new user
        writeNewUser(user.getUid(), name, lastname, user.getEmail());

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(RegisterActivity.this, "Account activatie e-mail verstuurd.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Go to MainActivity
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
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
            mNameField.setError("Vul naam in.");
            result = false;
        } else {
            mNameField.setError(null);
        }

        if (TextUtils.isEmpty(mLastNameField.getText().toString())) {
            mLastNameField.setError("Vul achternaam in.");
            result = false;
        } else {
            mLastNameField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Vul wachtwoord in.");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    // [START basic_write]
    public void writeNewUser(String uid, String Name, String last_name, String email) {
        User user = new User(Name, last_name, email);

        int hash = email.hashCode();
        String hash_email = String.valueOf(hash);

        mDatabase.child("users").child(hash_email).child("userinfo").setValue(user);
    }
    // [END basic_write]

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.register_button) {
            signUp();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
