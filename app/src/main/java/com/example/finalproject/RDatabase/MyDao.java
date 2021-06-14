package com.example.finalproject.RDatabase;


import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MyDao {

    @Query("select * from UserDatabase ")
    List<UserDatabase> getAllData();

    @Insert
    void insertData(UserDatabase nd);

    @Update
    void updateData(UserDatabase nd);

    @Delete
    void deleteData(UserDatabase nd);

}