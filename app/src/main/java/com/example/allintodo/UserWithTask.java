package com.example.allintodo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/*
 This class represents the One - Many Relationship Between User and Task Entities
 */
public class UserWithTask {
    @Embedded public User user;
    @Relation(
            parentColumn = "email",
            entityColumn = "userCreatorId"
    )
    public List<Task> taskList;

    public UserWithTask(User user, List<Task> taskList) {
        this.user = user;
        this.taskList = taskList;
    }
}
