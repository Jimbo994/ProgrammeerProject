/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 */

package mprog.nl.studentenschoonmaakapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mprog.nl.studentenschoonmaakapp.login.LoginActivity;
import mprog.nl.studentenschoonmaakapp.myaccount.MyAccountActivity;
import mprog.nl.studentenschoonmaakapp.models.Group;

/**
 * Shows ListView with all groups a user is a member of in FireBase Database. These groups can be
 * deleted on longItemClick and contents of the group can be seen onItemClick.
 * Via a floating action button a group can be added through opening MakeGroupActivity.
 * This Activity contains a NavigationDrawer with which can be navigated to MyAccountActivity.
 * There is also a sign out button in the Navigation drawer. The email of the currently signed in
 * user can be seen too.
 */

public class MyGroupsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabase;
    private ListView mGroups;

    String hash_current_user;
    String email_current_user;

    private FirebaseListAdapter mAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Auth.
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //  retrieve current users email and hash it for Database reference of user.
        if (auth.getCurrentUser() != null){
            email_current_user = auth.getCurrentUser().getEmail();
            hash_current_user = String.valueOf((email_current_user.hashCode()));
        } else{
         FirebaseAuth.getInstance().signOut();
           startActivity(new Intent(this, LoginActivity.class));
           finish();
        }

        // Initialize FireBase Database.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").
                child(hash_current_user).child("groups");
        //Views.
        mGroups = (ListView) findViewById(R.id.listview_mygroups);

        // Floating Action button.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_group_button);
        //onClick a group can be added in MakeGroupActivity.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyGroupsActivity.this, MakeGroupActivity.class));
            }
        });

        inflateNavigationDrawer();
        setListView();
        setListViewOnItemClickListener();
        setListViewOnLongItemClickListener();
    }

    private void inflateNavigationDrawer() {
        //  Inflate navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Set view and click listener.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set header text to currently logged in user.
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.username_Textview);
        nav_user.setText(email_current_user);
    }

    private void setListView() {
        // FireBaseListAdapter that loads groups of a user.
        mAdapter = new FirebaseListAdapter<Group>
                (this, Group.class, R.layout.custom_listview_groups, mDatabase) {
            @Override
            protected void populateView(View v, Group model, int position) {
                TextView group = (TextView) v.findViewById(R.id.group_name);
                String group_name = model.getGroupName();
                group.setText(group_name);
            }
        };
        // Set adapter on ListView.
        mGroups.setAdapter(mAdapter);
    }

    private void setListViewOnItemClickListener() {
        // OnItemClickListener Starts intent RoomActivity and sends through clicked groupId and name.
        mGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent groupdetail = new Intent(getApplicationContext(), RoomActivity.class);
                Group group = (Group) mGroups.getItemAtPosition(i);
                groupdetail.putExtra("groepnaam", group.getGroupName());
                groupdetail.putExtra("groepid", group.getGroupId());
                startActivity(groupdetail);
            }
        });
    }

    private void setListViewOnLongItemClickListener() {
        // OnItemLongClickListener inflates dialog where user can delete his/her group.
        mGroups.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Group group = (Group) mGroups.getItemAtPosition(i);
                final String group_id = group.getGroupId();
                final Dialog dialog = new Dialog(MyGroupsActivity.this);
                dialog.setContentView(R.layout.custom_dialog_remove_group);

                // Buttons in Dialog.
                Button remove = (Button) dialog.findViewById(R.id.remove_button);
                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteGroup(group_id);
                        mAdapter.notifyDataSetChanged();
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
    }

    private void deleteGroup(final String group_id) {
        final DatabaseReference ref_groups = FirebaseDatabase.getInstance().getReference().child("groups");
        // Deleting group under users.
        ref_groups.child(group_id).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String current_member_id = ds.getValue(String.class);
                    FirebaseDatabase.getInstance().getReference().child("users").child(current_member_id).child("groups").
                            child(group_id).removeValue();
                }
                // Deleting of group under groups.
                ref_groups.child(group_id).removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    // Closes Navigation Drawer onBackPressed.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Handles Navigation view item clicks.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.nav_lists:
                break;
            case R.id.nav_account:
                Intent myAccount = (new Intent(this, MyAccountActivity.class));
                myAccount.putExtra("userhash", hash_current_user);
                myAccount.putExtra("email", email_current_user);
                startActivity(myAccount);
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
}
