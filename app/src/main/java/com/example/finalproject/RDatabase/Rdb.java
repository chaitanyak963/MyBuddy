package com.example.finalproject.RDatabase;



import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserDatabase.class},version = 1,exportSchema = false)
public abstract class Rdb extends RoomDatabase {
    public abstract MyDao myDao();
}