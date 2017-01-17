package mprog.nl.studentenschoonmaakapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MakeGroupActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> mMemberList;

    EditText mNameField;
    EditText mEmailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMemberList = new ArrayList<>();

        // Buttons
        findViewById(R.id.cancel_button).setOnClickListener(this);
        findViewById(R.id.save_button).setOnClickListener(this);

        // ListView
        final ListView MemberList = (ListView) findViewById(R.id.MemberList);

        // ArrayList
        //mMemberList = getIntent().getStringArrayListExtra("MemberList");


        //ArrayAdapter
        final ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mMemberList);
        if (mMemberList != null){
            MemberList.setAdapter(arrayAdapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.invite_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MakeGroupActivity.this);
                dialog.setContentView(R.layout.custom_dialog);

                // Edittext in AlertDialog
                mNameField = (EditText) dialog.findViewById(R.id.field_invite_member_name);
                mEmailField = (EditText) dialog.findViewById(R.id.field_invite_member_email);


                // Buttons in Alertdialog
                Button invite = (Button) dialog.findViewById(R.id.invite_member_button);
                Button cancel = (Button) dialog.findViewById(R.id.invite_cancel_button);

                dialog.show();

                invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!validateForm()) {
                            return;
                        } else {
                            String email = mEmailField.getText().toString();
                            String name = mNameField.getText().toString();
                            mMemberList.add(name + ", " + email);
                            arrayAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(MakeGroupActivity.this, "Goed gedaan", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        MemberList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           final int index, long arg3) {

                LinearLayout layout = new LinearLayout(MakeGroupActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText memberEditText = new EditText(MakeGroupActivity.this);
                layout.addView(memberEditText);
                memberEditText.setHint("Bijnaam");

                final EditText emailEditText = new EditText(MakeGroupActivity.this);
                layout.addView(emailEditText);
                emailEditText.setHint("E-mailadres");

                AlertDialog dialog = new AlertDialog.Builder(MakeGroupActivity.this)
                        .setTitle("Edit Member")
                        .setView(layout)
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = String.valueOf(memberEditText.getText());
                                String email = String.valueOf(emailEditText.getText());
                                mMemberList.remove(index);
                                mMemberList.add(email + ", " + name);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mMemberList.remove(index);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .create();
                dialog.show();

                return true;
//                mMemberList.remove(index);
//                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    private void writeNewGroup() {
        Toast.makeText(this, "Goed gedaan", Toast.LENGTH_SHORT).show();

        // hier moet de groep aangemaakt worden in de firebase.

        // hier moeten ook alle emails verstuurd worden.
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.cancel_button) {
            finish();
        } else if (i == R.id.save_button) {
            writeNewGroup();
            finish();
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Vul e-mail in.");
            result = false;
        } else {
            mEmailField.setError(null);
        }
        if (TextUtils.isEmpty(mNameField.getText().toString())) {
            mNameField.setError("Vul bijnaam in.");
            result = false;
        } else {
            mEmailField.setError(null);
        }
        return result;
    }
}


