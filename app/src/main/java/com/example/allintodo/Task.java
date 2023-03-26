package com.example.allintodo;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Task {
    @NonNull
    @PrimaryKey
    int id;

    String name;
    Date dueDate;
}