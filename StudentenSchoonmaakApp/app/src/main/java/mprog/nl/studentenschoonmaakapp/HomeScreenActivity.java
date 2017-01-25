package mprog.nl.studentenschoonmaakapp;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import mprog.nl.studentenschoonmaakapp.models.FireBaseHelper;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    ArrayList mMyGroups;

    FireBaseHelper mHelper;

    ListView mGroups;

    ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mijn Groepen");

        mAuth = FirebaseAuth.getInstance();

        mGroups = (ListView) findViewById(R.id.listview_mygroups);

        final String email_current_user = mAuth.getCurrentUser().getEmail();
        final String hash_current_user = String.valueOf((email_current_user.hashCode()));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(hash_current_user).child("groups");

        mHelper = new FireBaseHelper(mDatabase);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mHelper.retrieve());

        final ArrayList mGroupid = mHelper.retrieve_groupid();

        mGroups.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        mGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent groupdetail = new Intent (getApplicationContext(), GroupActivity.class);
                groupdetail.putExtra("groepnaam", mGroups.getItemAtPosition(i).toString());
                groupdetail.putExtra("groepid", mGroupid.get(i).toString());
                startActivity(groupdetail);
            }
        });

        mGroups.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String group_name = mGroups.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), "kamer" + group_name, Toast.LENGTH_SHORT).show();

                final Dialog dialog = new Dialog(HomeScreenActivity.this);
                dialog.setContentView(R.layout.custom_dialog_remove_group);

                // Buttons in Alertdialog
                Button remove = (Button) dialog.findViewById(R.id.remove_button);
                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                return true;
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
