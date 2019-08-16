package com.example.login_addnote.notethings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper1 extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public DatabaseHelper1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db1) {

        // create notes table
        db1.execSQL(Note.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db1, int oldVersion, int newVersion) {
        // Drop older table if existed
        db1.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Create tables again
        onCreate(db1);
    }

    public long insertNote(String title, String details) {
        // get writable database as we want to write data
        SQLiteDatabase db1 = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Note.COLUMN_TITLE,title);
        values.put(Note.COLUMN_DETAILS,details);

        // insert row
        long id = db1.insert(Note.TABLE_NAME, null, values);

        // close db1 connection
        db1.close();

        // return newly inserted row id
        return id;
    }

    public Note getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db1 = this.getReadableDatabase();

        Cursor cursor = db1.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_TITLE, Note.COLUMN_DETAILS},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_DETAILS)));

        // close the db1 connection
        cursor.close();

        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME;

        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor cursor = db1.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TITLE)));
                note.setDetails(cursor.getString(cursor.getColumnIndex(Note.COLUMN_DETAILS)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db1 connection
        db1.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNote(Note title,Note details) {
        SQLiteDatabase db1 = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_TITLE, title.getTitle());
        values.put(Note.COLUMN_DETAILS,details.getDetails());

        // updating row
        return db1.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(title.getId())});
    }

    public void deleteNote(Note title) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(title.getId())});
        db1.close();
    }


}
