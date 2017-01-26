package mprog.nl.studentenschoonmaakapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mprog.nl.studentenschoonmaakapp.models.User;

public class EditMyAccountActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    EditText mName;
    EditText mLastName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_account);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        //EditTexts
        mName = (EditText) findViewById(R.id.edit_my_account_field_name);
        mLastName = (EditText) findViewById(R.id.edit_my_account_field_lastname);

        //Button
        Button save = (Button) findViewById(R.id.save_edit_my_account);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                } else {
                    String name = mName.getText().toString();
                    String lastname = mLastName.getText().toString();
                    String email = mAuth.getCurrentUser().getEmail();
                    User user = new User();
                    user.setName(name);
                    user.setLastname(lastname);
                    user.setEmail(email);

                    int hash = email.hashCode();
                    String hash_email = String.valueOf(hash);

                    mDatabase.child(hash_email).child("userinfo").setValue(user);
                }
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mLastName.getText().toString())) {
            mLastName.setError("Vul een achternaam in.");
            result = false;
        } else {
            mLastName.setError(null);
        }
        if (TextUtils.isEmpty(mName.getText().toString())) {
            mName.setError("Vul bijnaam in.");
            result = false;
        } else {
            mName.setError(null);
        }
        return result;
    }

}
