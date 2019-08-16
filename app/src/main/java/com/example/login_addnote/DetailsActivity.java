package com.example.login_addnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_addnote.notethings.DatabaseHelper1;
import com.example.login_addnote.notethings.Note;
import com.example.login_addnote.notethings.NotesAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private DatabaseHelper1 db2;
    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();

    TextView titletext,desctext;
    TextView showphotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        titletext=(TextView)findViewById(R.id.titletext);
        desctext=(TextView)findViewById(R.id.desctext);
        showphotos=(TextView) findViewById(R.id.watchphotos);


        db2 = new DatabaseHelper1(this);

        notesList.addAll(db2.getAllNotes());
        mAdapter = new NotesAdapter(this, notesList);


        showphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoactivity=new Intent(getApplicationContext(),Blankphoto.class);
                startActivity(photoactivity);
            }
        });

        Intent intent=getIntent();

        String title=intent.getStringExtra("title");
        String details=intent.getStringExtra("details");

        titletext.setText(title);
        desctext.setText(details);

    }
}
