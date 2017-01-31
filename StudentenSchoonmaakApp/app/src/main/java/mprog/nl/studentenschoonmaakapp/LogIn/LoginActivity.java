/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 *
 * This code was inspired by google tutorial code for FireBase (see URL below)
 *  https://github.com/firebase/quickstart-android
 */

package mprog.nl.studentenschoonmaakapp.LogIn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import mprog.nl.studentenschoonmaakapp.BaseActivity;
import mprog.nl.studentenschoonmaakapp.MyGroupsActivity;
import mprog.nl.studentenschoonmaakapp.R;

/**
 * Does Sign in on FireBase database, extends BaseActivity for showing of ProgressDialog.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEmailField;
    private EditText mPasswordField;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        mEmailField = (EditText) findViewById(R.id.field_login_email);
        mPasswordField = (EditText) findViewById(R.id.field_login_password);

        // Buttons
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.password_forgotten).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);

        // initialize mAuth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess();
        }
    }

    // Signs in user if validateForm and credentials are correct.
    private void signIn() {
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
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            onAuthSuccess();
                            Toast.makeText(LoginActivity.this, "Inloggen...",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Inloggen mislukt.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // If successfully logged in, go to MyGroupsActivity
    private void onAuthSuccess() {
        startActivity(new Intent(LoginActivity.this, MyGroupsActivity.class));
        finish();
    }

    // Checks if Textfield are properly filled in.
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
        switch (view.getId()){
            case R.id.login_button:
                signIn();
                break;
            case R.id.password_forgotten:
                startActivity(new Intent(LoginActivity.this, PasswordRecoveryActivity.class));
                break;
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }
}

