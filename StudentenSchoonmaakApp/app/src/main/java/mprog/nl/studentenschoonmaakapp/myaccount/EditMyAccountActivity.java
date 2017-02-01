/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 */

package mprog.nl.studentenschoonmaakapp.myaccount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mprog.nl.studentenschoonmaakapp.R;
import mprog.nl.studentenschoonmaakapp.models.User;

/**
 * Edits Userinfo of user.
 */

public class EditMyAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText mName;
    private EditText mLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase DatabaseReference and Auth.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        //EditTexts.
        mName = (EditText) findViewById(R.id.edit_my_account_field_name);
        mLastName = (EditText) findViewById(R.id.edit_my_account_field_lastname);

        //Button.
        Button save = (Button) findViewById(R.id.save_edit_my_account);

        // Gets data from EditText fields and sets this data as new userinfo in Database.
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
                    User user = new User();
                    user.setName(mName.getText().toString());
                    user.setLastName(mLastName.getText().toString());
                    user.setEmail(mAuth.getCurrentUser().getEmail());

                    int hash = mAuth.getCurrentUser().getEmail().hashCode();
                    String hash_email = String.valueOf(hash);

                    mDatabase.child(hash_email).child("userinfo").setValue(user);
                    finish();
                }
        });
    }

    // Checks if TextFields are properly filled in.
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mLastName.getText().toString())) {
            mLastName.setError(getString(R.string.vulachternaamin));
            result = false;
        } else {
            mLastName.setError(null);
        }
        if (TextUtils.isEmpty(mName.getText().toString())) {
            mName.setError(getString(R.string.vulnaamin));
            result = false;
        } else {
            mName.setError(null);
        }
        return result;
    }

    // Finishes activity on navigate back button click.
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
