package com.example.login_addnote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Blankphoto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blankphoto);
        //hiding actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}
