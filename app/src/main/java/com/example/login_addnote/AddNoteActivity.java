package com.example.login_addnote;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.login_addnote.notethings.GalleryAdapter;
import com.example.login_addnote.notethings.Note;
import com.example.login_addnote.notethings.NotesAdapter;
import com.example.login_addnote.notethings.DatabaseHelper1;


public class AddNoteActivity extends AppCompatActivity {

    Button addnote, addphoto;
    EditText title, details;

    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private DatabaseHelper1 db1;

    //for storing photo and showing
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        db1 = new DatabaseHelper1(this);
        notesList.addAll(db1.getAllNotes());
        mAdapter = new NotesAdapter(this, notesList);


        addnote = (Button) findViewById(R.id.addnote);
        addphoto = (Button) findViewById(R.id.addphoto);
        title = (EditText) findViewById(R.id.title);
        details = (EditText) findViewById(R.id.details);

        //for photo
        gvGallery = (GridView)findViewById(R.id.gv);



        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title1 = title.getText().toString().trim();
                String detail1 = details.getText().toString().trim();

                if (title1.matches("") || detail1.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter all fields!", Toast.LENGTH_SHORT).show();
                }
                else {

                    try {
                        createNote(title1, detail1);
                        Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to insert", Toast.LENGTH_SHORT).show();
                    }
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);


                }

            }
        });
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_MULTIPLE);
            }
        });
    }

    /**
     * Inserting new note in db1
     * and refreshing the list
     */
    private void createNote(String note, String details) {
        // inserting note in db1 and getting
        // newly inserted note id
        long id = db1.insertNote(note,details);

        // get the newly inserted note from db1
        Note n = db1.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

        }
    }

    //for saving photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                            galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
