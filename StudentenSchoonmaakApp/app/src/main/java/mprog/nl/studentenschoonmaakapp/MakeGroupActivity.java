package mprog.nl.studentenschoonmaakapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MakeGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> mMemberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Buttons
        findViewById(R.id.cancel_button).setOnClickListener(this);
        findViewById(R.id.save_button).setOnClickListener(this);

        // ListView
        final ListView MemberList = (ListView) findViewById(R.id.MemberList);

        // ArrayList
        mMemberList = getIntent().getStringArrayListExtra("MemberList");

        //ArrayAdapter
        final ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mMemberList);
        if (mMemberList != null){
            MemberList.setAdapter(arrayAdapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.invite_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MakeGroupActivity.this, InviteActivity.class));
            }
        });

        MemberList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                mMemberList.remove(index);
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    private void writeNewGroup() {
        Toast.makeText(this, "Goed gedaan", Toast.LENGTH_SHORT).show();

        // hier moet de groep aangemaakt worden in de firebase
        // hier moeten ook alle emails verstuurd worden.

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.cancel_button) {
            finish();
        } else if (i == R.id.save_button) {
            writeNewGroup();
        }
    }
}


