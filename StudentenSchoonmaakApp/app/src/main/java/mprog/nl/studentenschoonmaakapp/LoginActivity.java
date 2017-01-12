/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 *
 * This code was inspired by google tutorial code for FireBase (see URL below)
 *  https://github.com/firebase/quickstart-android
 */

package mprog.nl.studentenschoonmaakapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
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

/**
 * Does sign in and registration on FireBase database, extends BaseActivity for showing of ProgressDialog.
 * Uses User model for writing user into Firebase database.
 */

public class LoginActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "LogIn";

    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Inloggen");

        // Views
        mEmailField = (EditText) findViewById(R.id.field_login_email);
        mPasswordField = (EditText) findViewById(R.id.field_login_password);

        // Buttons
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.password_forgotten).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                            Toast.makeText(LoginActivity.this, "Inloggen...",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Inloggen mislukt.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        // Go to MainActivity
        startActivity(new Intent(LoginActivity.this, HomeScreenActivity.class));
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

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Vul wachtwoord in.");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.login_button) {
            signIn();
        } else if (i == R.id.password_forgotten) {
            startActivity(new Intent(LoginActivity.this, PasswordRecoveryActivity.class));
        } else if (i == R.id.register) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    }
}

