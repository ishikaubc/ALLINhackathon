package com.example.allintodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskView extends AppCompatActivity {
    AppDatabase db;
    ArrayList<Task> tasks;
    ListView taskView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        //Get Views
        taskView = (ListView) findViewById(R.id.taskView);

        //Pull info from intent
        Intent i = getIntent();
        Bundle b = i.getExtras();
        String email = b.getString("email");

        //Access DB and get dao
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db").build();

        //Go into thread for DB acess

        Thread tr = new Thread(new Runnable(){
            @Override
            public void run(){

                UserDao userDao = db.userDao();
                //Query for the user
                User user = userDao.getUser(email);
                if( userDao.getUserTasks(email) != null){
                    tasks = (ArrayList<Task>) userDao.getUserTasks(email).taskList;
                }else{
                    Log.i("DEBUG", "User has no tasks");
                }
            }
        });
        tr.start();



        //DEBUG LIST
        Task t1 = new Task("task1", email, "This is task 1", null);
        //DEBUG LIST
        Task t2 = new Task("task2", email, "This is task 2", null);
        //DEBUG LIST
        Task t3 = new Task("task3", email, "This is task 3", null);

        tasks = new ArrayList<Task>();
        tasks.add(t1);
        tasks.add(t2);
        tasks.add(t3);

        //DEBUG !!!! Print out all of the tasks
        for(Task t: tasks){
            Log.i("DEBUG", t.toString());
        }
        //Setup the list view with the custom task list adapter
        taskView.setAdapter(new TaskViewAdapter(this, tasks));
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