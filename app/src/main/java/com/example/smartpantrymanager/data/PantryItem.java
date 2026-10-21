package com.example.smartpantrymanager.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "pantry_items")
public class PantryItem {


    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name;            // what the user typed, e.g. "Onion"

    private double quantity;        // e.g. 3

    @NonNull
    private String unit;            // e.g. "pcs"

    @Nullable
    private String expiryDate;      // optional, stored as dd-MM-yyyy

    public PantryItem(@NonNull String name, double quantity,
                      @NonNull String unit, @Nullable String expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    @NonNull
    public String getUnit() { return unit; }
    public void setUnit(@NonNull String unit) { this.unit = unit; }

    @Nullable
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(@Nullable String expiryDate) { this.expiryDate = expiryDate; }
}