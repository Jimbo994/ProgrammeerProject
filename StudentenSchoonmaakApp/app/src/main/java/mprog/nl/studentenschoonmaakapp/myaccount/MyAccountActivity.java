/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 */
package mprog.nl.studentenschoonmaakapp.myaccount;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mprog.nl.studentenschoonmaakapp.login.LoginActivity;
import mprog.nl.studentenschoonmaakapp.MyGroupsActivity;
import mprog.nl.studentenschoonmaakapp.R;
import mprog.nl.studentenschoonmaakapp.models.User;

/**
 * This Activity shows currently signed in users details. It also has a edit button which redirects
 * to EditMyAccountActivity where the user can change his user information. There is also a
 * Navigation drawer which can redirect the user to MyGroupsActivity and SignOut.
 */

public class MyAccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView mEmail;
    private TextView mFirstName;
    private TextView mLastName;
    private DatabaseReference mDatabase;
    private Toolbar mToolbar;

    String email;
    String hash_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize FireBase DatabaseReference.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // TextViews.
        mEmail = (TextView) findViewById(R.id.my_account_field_email);
        mFirstName = (TextView) findViewById(R.id.my_account_field_name);
        mLastName = (TextView) findViewById(R.id.my_account_field_lastname);

        // Buttons.
        findViewById(R.id.my_account_edit_button).setOnClickListener(this);

        // Retrieve email and hash of currently signed in user.
        hash_email = getIntent().getStringExtra("userhash");
        email = getIntent().getStringExtra("email");

        inflateNavigationDrawer();
        fillTextViews();
    }

    private void inflateNavigationDrawer() {
        // Inflate Navigation drawer.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Set view and click listener.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set header text to currently logged in user.
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.username_Textview);
        nav_user.setText(email);
    }

    private void fillTextViews() {
        // Retrieve current userinfo from DataBase and setText into TextViews.
        mDatabase.child(hash_email).child("userinfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mEmail.setText(user.getEmail());
                mFirstName.setText(user.getName());
                mLastName.setText(user.getLastName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_account_edit_button:
                startActivity(new Intent(MyAccountActivity.this, EditMyAccountActivity.class));
            }
        }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            case R.id.nav_lists:
                startActivity(new Intent(this, MyGroupsActivity.class));
                break;
            case R.id.nav_account:
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Finishes activity on navigate back button click.
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
