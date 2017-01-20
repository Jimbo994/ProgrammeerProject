package mprog.nl.studentenschoonmaakapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mprog.nl.studentenschoonmaakapp.models.FireBaseHelper;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    FireBaseHelper mHelper;

    ListView mGroups;

    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mijn Groepen");

        mAuth = FirebaseAuth.getInstance();

        final String uid_current_user = mAuth.getCurrentUser().getUid();

        mGroups = (ListView) findViewById(R.id.listview_mygroups);

        String email_current_user = mAuth.getCurrentUser().getEmail();
        String hash_current_user = String.valueOf((email_current_user.hashCode()));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(hash_current_user).child("groups");

        mHelper = new FireBaseHelper(mDatabase);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mHelper.retrieve());

        mGroups.setAdapter(mAdapter);

        mGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent groupdetail = new Intent (getApplicationContext(), GroupDetailActivity.class);
                groupdetail.putExtra("groepnaam", mGroups.getItemAtPosition(i).toString());
                startActivity(groupdetail);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_group_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeScreenActivity.this, MakeGroupActivity.class));
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lists) {
            // Handle the camera action
        } else if (id == R.id.nav_account) {
            startActivity(new Intent(this, MyAccountActivity.class));
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
