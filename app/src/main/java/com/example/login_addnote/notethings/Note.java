package com.example.login_addnote.notethings;


public class Note {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DETAILS = "details";

    private int id;
    private String title;
    private String details;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_DETAILS + " TEXT"
                    + ")";

    public Note() {
    }

    public Note(int id, String title, String details) {
        this.id = id;
        this.title = title;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
