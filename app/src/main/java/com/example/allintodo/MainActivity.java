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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String m_Text;
    EditText emailText;
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
        emailText = (EditText) findViewById(R.id.Email);
        pass = (EditText) findViewById(R.id.Password);
        registerButton = (Button) findViewById(R.id.Signup);
        //Connect to db
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db").build();

    }

    public void loginClick(View v){
        final String userEmail = emailText.getText().toString(); //add from view element
        final String passwordText = pass.getText().toString(); //add from view element

        if(userEmail.isEmpty() || passwordText.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill in Fields!!!", Toast.LENGTH_SHORT).show();
        }else{
            //Perform Query to DB
            final UserDao userDao = db.userDao();
            Thread tr = new Thread(new Runnable(){
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
                                .putExtra("email",userEmail));
                    }
                }
            });
            tr.start();
        }
    }

    public void signUp(View v){
        final String userEmail = emailText.getText().toString(); //add from view element
        final String passwordText = pass.getText().toString(); //add from view element

        if(userEmail.isEmpty() || passwordText.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill in Fields!!!", Toast.LENGTH_SHORT).show();
        }else{
            //Perform Query to DB
            final UserDao userDao = db.userDao();
            Log.i("DEBUG","got the USER DAO");
            Thread tr = new Thread(new Runnable(){
                @Override
                public void run(){
                    Log.i("DEBUG", "run thread");
                    //Register user to Data base
                    User user = new User(userEmail, passwordText, "first Name", "fred");
                    Log.i("DEBUG", "User Created, name: " + user.firstName);
                    userDao.register(user);
                    Log.i("DEBUG", "Registered User");
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
                                .putExtra("email",userEmail));
                    }
                }
            });
            tr.start();
        }
    }

}
