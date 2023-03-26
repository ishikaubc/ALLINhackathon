package com.example.allintodo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE email IN (:emails)")
    List<User> loadAllByIds(int[] emails);

    @Query("SELECT * FROM user WHERE email LIKE :email LIMIT 1")
    User login(String email);

    @Insert
    void register(User user);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}