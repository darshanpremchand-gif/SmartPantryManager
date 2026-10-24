package com.example.smartpantrymanager.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// the tables in my database
@Database(entities = {PantryItem.class, Recipe.class, RecipeIngredient.class},
        version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // This gives access to the pantry actions
    public abstract PantryItemDao pantryItemDao();

    // This gives access to the recipe actions
    public abstract RecipeDao recipeDao();

    // only one copy of the database
    private static AppDatabase instance;

    // creates the database the first time, then reuses it
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "smart_pantry_db")
                    .fallbackToDestructiveMigration() // wipes old data if the version changes
                    .build();
        }
        return instance;
    }
}