/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 *
 * This code was inspired by google tutorial code for FireBase (see URL below)
 *  https://github.com/firebase/quickstart-android
 */
package mprog.nl.studentenschoonmaakapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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

import mprog.nl.studentenschoonmaakapp.models.BaseActivity;
import mprog.nl.studentenschoonmaakapp.R;
import mprog.nl.studentenschoonmaakapp.models.User;

/**
 * Does Sign in on FireBase database, extends BaseActivity for showing of ProgressDialog.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText mEmailField;
    private EditText mNameField;
    private EditText mLastNameField;
    private EditText mPasswordField;

    public String name;
    public String lastname;
    public String email;
    public String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize DatabaseReference and Auth.
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views.
        mEmailField = (EditText) findViewById(R.id.field_register_email);
        mNameField = (EditText) findViewById(R.id.field_register_name);
        mLastNameField = (EditText) findViewById(R.id.field_register_lastname);
        mPasswordField = (EditText) findViewById(R.id.field_register_password);

        // Buttons.
        findViewById(R.id.register_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_button:
                signUp();
        }
    }

    // Registers new user, writes into database and sends Account Activation email.
    private void signUp() {
        if (!validateForm()) {
            return;
        }
        showProgressDialog();

        email = mEmailField.getText().toString();
        name = mNameField.getText().toString();
        lastname = mLastNameField.getText().toString();
        password = mPasswordField.getText().toString();

        // Register users and call onAuthSuccess if successful.
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                            Toast.makeText(RegisterActivity.this, R.string.registreren,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, R.string.registerenmislukt,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Calls writeNewUser then sends Account Activation email. Logs in user.
    private void onAuthSuccess(FirebaseUser user) {
        writeNewUser(name, lastname, user.getEmail());
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, R.string.emailverstuurd,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Go to MainActivity.
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    // Writes user into database.
    public void writeNewUser(String Name, String last_name, String email) {
        User user = new User(Name, last_name, email);

        // Make hash from email and set as user id.
        int hash = email.hashCode();
        String hash_email = String.valueOf(hash);

        mDatabase.child("users").child(hash_email).child("userinfo").setValue(user);
    }


    // Checks if Textfields are properly filled in.
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError(getString(R.string.vulemailin));
            result = false;
        } else {
            mEmailField.setError(null);
        }if (TextUtils.isEmpty(mNameField.getText().toString())) {
            mNameField.setError(getString(R.string.vulnaamin));
            result = false;
        } else {
            mNameField.setError(null);
        } if (TextUtils.isEmpty(mLastNameField.getText().toString())) {
            mLastNameField.setError(getString(R.string.vulachternaamin));
            result = false;
        } else {
            mLastNameField.setError(null);
        } if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError(getString(R.string.vulwachtwoord));
            result = false;
        } else {
            mPasswordField.setError(null);
        }
        return result;
    }

    // Finishes activity on navigate back button click.
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
