package com.example.login_addnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_addnote.notethings.MyDividerItemDecoration;
import com.example.login_addnote.notethings.Note;
import com.example.login_addnote.notethings.NotesAdapter;
import com.example.login_addnote.notethings.DatabaseHelper1;
import com.example.login_addnote.notethings.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;
    private DatabaseHelper1 db1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noNotesView = findViewById(R.id.empty_notes_view);


        db1 = new DatabaseHelper1(this);

        notesList.addAll(db1.getAllNotes());
        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                senddata(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db1.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private void updateNote(String note,String details, int position) {
        Note n = notesList.get(position);
        // updating note text
        n.setTitle(note);
        n.setDetails(details);

        // updating note in db1
        db1.updateNote(n,n);

        // refreshing the list
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

public void senddata(final int position)
{
        sendall(notesList.get(position),position);
}

public void sendall(final Note note,final int position)
{
        String titlesend=note.getTitle();
        String detailsend=note.getDetails();

     //   Toast.makeText(getApplicationContext(),titlesend,Toast.LENGTH_SHORT).show();

        Intent towardsdetails=new Intent(getApplicationContext(),DetailsActivity.class);
        towardsdetails.putExtra("title",titlesend);
        towardsdetails.putExtra("details",detailsend);
        startActivity(towardsdetails);

}

}
