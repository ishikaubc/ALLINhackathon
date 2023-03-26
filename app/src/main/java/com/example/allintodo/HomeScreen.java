package com.example.allintodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Just passing through
        Intent i = getIntent();
        String email = i.getStringExtra("email");
        Log.i("DEBUG","EMAIL: " + email);

        Intent intent = new Intent(this, TaskView.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}