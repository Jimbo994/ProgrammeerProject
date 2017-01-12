package mprog.nl.studentenschoonmaakapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordRecoveryActivity extends BaseActivity implements
        View.OnClickListener  {

    private static final String TAG = "Recover";

    private EditText mEmailField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        getSupportActionBar().setTitle("Herstel mijn wachtwoord");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Views
        mEmailField = (EditText) findViewById(R.id.field_recovery_email);

        //Buttons
        findViewById(R.id.recovery_button).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }


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
                            Log.d(TAG, "Email sent.");
                        } else {
                            Toast.makeText(PasswordRecoveryActivity.this, "Er is iets misgegaan.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
