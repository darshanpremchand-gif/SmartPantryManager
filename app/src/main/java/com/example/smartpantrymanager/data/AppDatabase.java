package com.example.smartpantrymanager.data;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// the tables in my database (just pantry items for now)
@Database(entities = {PantryItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // gives access to the pantry actions
    public abstract PantryItemDao pantryItemDao();

    // only one copy of the database
    private static AppDatabase instance;

    // creates the database the first time, then reuses it
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "smart_pantry_db")
                    .build();
        }
        return instance;
    }
}
