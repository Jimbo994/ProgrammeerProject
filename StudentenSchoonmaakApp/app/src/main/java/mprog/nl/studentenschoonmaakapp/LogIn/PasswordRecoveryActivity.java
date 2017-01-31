/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 *
 * This code was inspired by google tutorial code for FireBase (see URL below)
 *  https://github.com/firebase/quickstart-android
 */
package mprog.nl.studentenschoonmaakapp.LogIn;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import mprog.nl.studentenschoonmaakapp.BaseActivity;
import mprog.nl.studentenschoonmaakapp.R;

/**
 * Send password reset email to email that has been put in.
 */

public class PasswordRecoveryActivity extends BaseActivity implements
        View.OnClickListener  {

    private FirebaseAuth mAuth;
    private EditText mEmailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Views
        mEmailField = (EditText) findViewById(R.id.field_recovery_email);

        //Buttons
        findViewById(R.id.recovery_button).setOnClickListener(this);

        // Initialize Auth
        mAuth = FirebaseAuth.getInstance();
    }

    // Sends password reset email.
    @Override
    public void onClick(View view) {

        String emailAddress = mEmailField.getText().toString();

        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PasswordRecoveryActivity.this, "E-mail verzonden.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PasswordRecoveryActivity.this, "Er is iets misgegaan.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Finishes activity on navigate back button click.
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
