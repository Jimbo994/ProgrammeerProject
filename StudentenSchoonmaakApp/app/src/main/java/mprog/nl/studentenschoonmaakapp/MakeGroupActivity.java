package mprog.nl.studentenschoonmaakapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MakeGroupActivity extends AppCompatActivity implements View.OnClickListener {


    private ArrayList<String> text;

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
        ListView MemberList = (ListView) findViewById(R.id.MemberList);

        // ArrayList
        text = (ArrayList<String>) getIntent().getStringArrayListExtra("MemberList");

        //ArrayAdapter
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, text);
        if (text != null){
            MemberList.setAdapter(arrayAdapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.invite_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MakeGroupActivity.this, InviteActivity.class));
            }
        });
    }

    private void writeNewUser() {
        Toast.makeText(this, "Goed gedaan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.cancel_button) {
            finish();
        } else if (i == R.id.save_button) {
            writeNewUser();
        }
    }
}


