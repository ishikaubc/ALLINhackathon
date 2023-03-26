package com.example.allintodo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String m_Text;
    EditText nameText;
    EditText pass;
    Button addButton;
    Button registerButton;
    ListView mainList;
    String [] names;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        names = new String [10];
        nameText = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.register);
        //Connect to db
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db").build();

    }

    public void loginClick(View v){
        final String userEmail = nameText.getText().toString(); //add from view element
        final String passwordText = pass.getText().toString(); //add from view element

        if(userEmail.isEmpty() || passwordText.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill in Fields!!!", Toast.LENGTH_SHORT).show();
        }else{
            //Perform Query to DB
            final UserDao userDao = db.userDao();
            new Thread(new Runnable(){
                @Override
                public void run(){
                    User user = userDao.login(userEmail);
                    if(user == null){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        //Start up home screen
                        startActivity(new Intent(MainActivity.this, HomeScreen.class)
                                //Pass over user email
                                .putExtra("userEmail",userEmail));
                    }
                }
            });
        }
    }

    public void signUp(View v){
        final String userEmail = nameText.getText().toString(); //add from view element
        final String passwordText = pass.getText().toString(); //add from view element

        if(userEmail.isEmpty() || passwordText.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill in Fields!!!", Toast.LENGTH_SHORT).show();
        }else{
            //Perform Query to DB
            final UserDao userDao = db.userDao();
            new Thread(new Runnable(){
                @Override
                public void run(){

                    userDao.register(new User(userEmail, passwordText, "m_Text", ""));

                    if(false){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        //Start up home screen
                        startActivity(new Intent(MainActivity.this, HomeScreen.class)
                                //Pass over user email
                                .putExtra("userEmail",userEmail));
                    }
                }
            });
        }
    }
    public void onClick(View v){
        String name = nameText.toString();
        names[0] = name;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        mainList.setAdapter(adapter);


    }

    public void room (){

    }

}
