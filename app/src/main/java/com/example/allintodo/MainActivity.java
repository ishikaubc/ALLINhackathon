package com.example.allintodo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    EditText nameText;
    Button addButton;
    ListView mainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = (EditText) findViewById(R.id.name);
        addButton = (Button) findViewById(R.id.button);
        mainList = (ListView) findViewById(R.id.mainList);
    }

    public void onClick(View v){
        String name = nameText.toString();
        String [] names = {};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        mainList.setAdapter(ListAdapter);
    }
}