package com.example.allintodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskView extends AppCompatActivity {
    private AppDatabase db;
    private ArrayList<Task> tasks;
    private ListView taskView;
    private Button addButton, saveButton, cancelButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TaskViewAdapter taskViewAdapt;
    private TextView greet;
    private String email;
    //For popUp
    private EditText enterTitle, enterName, enterDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        tasks = new ArrayList<>();
        //Get Views
        taskView = (ListView) findViewById(R.id.taskView);
        addButton = (Button) findViewById(R.id.addButton);
        greet = (TextView) findViewById(R.id.tasks);

        //Pull info from intent
        Intent i = getIntent();
        email = i.getStringExtra("email");

        //Access DB and get dao
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db").build();

        //Go into thread for DB acess

        Thread tr = new Thread(new Runnable(){
            @Override
            public void run(){

                UserDao userDao = db.userDao();
                //Query for the user
                User user = userDao.getUser(email);
                greet.setText("Hello " + user.firstName);

                //Get all user tasks
                if(userDao.getUserWithTask(email) != null){
                    List<Task> tasksList = userDao.getUserWithTask(email).get(0).taskList;
                    tasks = (ArrayList<Task>) tasksList;
                    updateList();
                }

            }
        });
        tr.start();

        //Setup the list view with the custom task list adapter
        taskViewAdapt = new TaskViewAdapter(this, tasks);
        taskView.setAdapter(taskViewAdapt);
        taskView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int listItem, long l) {
                new AlertDialog.Builder(TaskView.this)
                        .setTitle("Do you want to remove " + tasks.get(listItem) + " from lsit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               //remove from db
                                Thread tr = new Thread(new Runnable(){
                                    @Override
                                    public void run(){

                                        UserDao userDao = db.userDao();
                                        userDao.delete(tasks.get(listItem));

                                    }
                                });
                                tr.start();
                                tasks.remove(listItem);
                               updateList();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                return false;
            }
        });

    }

    /*
        On click for the add button

        Used to add a task to the db for the signed in user
     */
    public void onAdd(View v) throws InterruptedException {
        dialogBuilder = new AlertDialog.Builder(this);
        final View taskPopup = getLayoutInflater().inflate(R.layout.popup, null);
        enterTitle = (EditText) taskPopup.findViewById(R.id.enterTodo);
        enterName = (EditText) taskPopup.findViewById(R.id.enterName);
        enterDate = (EditText) taskPopup.findViewById(R.id.enterDate);
        saveButton = (Button) taskPopup.findViewById(R.id.saveButton);
        cancelButton = (Button) taskPopup.findViewById(R.id.cancelButton);

        dialogBuilder.setView(taskPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Define Save Button
                //Go into thread for DB acess

                Thread tr = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        UserDao userDao = db.userDao();

                        //add task to db
                        Date date = null;
                        //Format input from pop up into task, and insert into DB
                        userDao.insert(new Task(enterName.getText().toString(), email, enterTitle.getText().toString(), date));


                        //Get all user tasks
                        List<Task> tasksList = userDao.getUserWithTask(email).get(0).taskList;
                        tasks = (ArrayList<Task>) tasksList;
                        Log.i("DEBUG", "Email: " + email);
                        for(Task t: tasksList){
                            Log.i("DEBUG", "Task: " + t.name);
                        }


                    }
                });
                tr.start();
                dialog.dismiss();
                updateList();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Define Cancel Button
                dialog.dismiss();
            }
        });
    }

    /*
        Update the List View
     */
    public void updateList(){
        Log.i("DEBUG", "Updating List");

        taskViewAdapt.clear();

        if (tasks != null){
            for (Task task : tasks) {

                taskViewAdapt.insert(task, taskViewAdapt.getCount());
            }
        }

        taskViewAdapt.notifyDataSetChanged();

    }
    /*
    Array Adapter for the task view
     */
    public class TaskViewAdapter extends ArrayAdapter<Task> {
        public TaskViewAdapter(@NonNull Context context, ArrayList<Task> tasks){
            super(context, 0, tasks);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            // convertView which is recyclable view
            View currentItemView = convertView;

            // of the recyclable view is null then inflate the custom layout for the same
            if (currentItemView == null) {
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.task_view, parent, false);
            }

            // get the position of the view from the ArrayAdapter
            Task task = getItem(position);

            assert task != null;

            // Set the task title
            TextView textView1 = currentItemView.findViewById(R.id.taskTitle);
            textView1.setText(task.title);

            // then return the recyclable view
            return currentItemView;
        }
    }
}