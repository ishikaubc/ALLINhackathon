package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void output(int id, String result){
        View view = findViewById(id);
        TextView textView = (TextView) view;
        textView.setText(result);
    }

    private String input(int id){
        View view = findViewById(id);
        EditText editText = (EditText) view;
        String str = editText.getText().toString();
        return str;
    }

    public void onStart(View view){
        Intent i = new Intent(this, MainActivity2.class);
        startActivity(i);
    }

}