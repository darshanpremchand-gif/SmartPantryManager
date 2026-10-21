package com.example.smartpantrymanager.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PantryItemDao {

    // create - add a new ingredient
    @Insert
    void insert(PantryItem item);

    // Read - get every ingredient, sorted A to Z
    @Query("SELECT * FROM pantry_items ORDER BY name ASC")
    List<PantryItem> getAllItems();

    // Read - get one ingredient by its id
    @Query("SELECT * FROM pantry_items WHERE id = :itemId")
    PantryItem getItemById(int itemId);

    // Update - save changes to an existing ingredient
    @Update
    void update(PantryItem item);

    // Delete - remove an ingredient
    @Delete
    void delete(PantryItem item);
}

