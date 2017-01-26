package mprog.nl.studentenschoonmaakapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mprog.nl.studentenschoonmaakapp.models.User;

public class MyAccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String TAG = "MyAccountActivity";
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    TextView mEmail;
    TextView mFirstName;
    TextView mLastName;

    String current_email;
    String current_first_name;
    String current_last_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mijn Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        //Textviews
        mEmail = (TextView) findViewById(R.id.my_account_field_email);
        mFirstName = (TextView) findViewById(R.id.my_account_field_name);
        mLastName = (TextView) findViewById(R.id.my_account_field_lastname);

        //Buttons
//        findViewById(R.id.button_delete_account).setOnClickListener(this);
        findViewById(R.id.button_reset_password).setOnClickListener(this);
        findViewById(R.id.my_account_edit_button).setOnClickListener(this);

        String email = mAuth.getCurrentUser().getEmail();

        int hash = email.hashCode();
        String hash_email = String.valueOf(hash);


        mDatabase.child(hash_email).child("userinfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Toast.makeText(MyAccountActivity.this, "User name: " + user.getName() + ", email " + user.getEmail(), Toast.LENGTH_LONG).show();
                current_email = user.getEmail();
                current_first_name = user.getName();
                current_last_name = user.getLastname();
                mEmail.setText(current_email);
                mFirstName.setText(current_first_name);
                mLastName.setText(current_last_name);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
//        if (i == R.id.button_delete_account) {
//            Toast.makeText(MyAccountActivity.this, "Nog implementeren", Toast.LENGTH_SHORT).show();
        if (i == R.id.button_reset_password) {

            mAuth.sendPasswordResetEmail(current_email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MyAccountActivity.this, "E-mail verzonden naar: " + current_email,
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Email sent.");
                            } else {
                                Toast.makeText(MyAccountActivity.this, "Er is iets misgegaan.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else if (i == R.id.my_account_edit_button) {
            startActivity(new Intent(MyAccountActivity.this, EditMyAccountActivity.class));
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        startActivity(new Intent(this, HomeScreenActivity.class));
        if (id == R.id.nav_lists) {
            // Handle the camera action
        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
