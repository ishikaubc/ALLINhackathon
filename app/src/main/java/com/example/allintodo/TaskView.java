package com.example.allintodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;

public class TaskView extends AppCompatActivity {
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        //Pull info from intent
        Intent i = getIntent();
        Bundle b = i.getExtras();
        String email = b.getString("email");

        //Access DB and get dao
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db").build();
        UserDao userDao = db.userDao();

        //Query for the user
        User user = userDao.getUser(email);


    }


}