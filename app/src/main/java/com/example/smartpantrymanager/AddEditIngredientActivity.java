package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.data.AppDatabase;
import com.example.smartpantrymanager.data.PantryItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText editName, editQuantity, editUnit, editExpiry;
    private Button buttonSave, buttonDelete;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // -1 means we are adding a new item
    private int itemId = -1;
    private PantryItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiry = findViewById(R.id.editExpiry);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        // check if MainActivity sent us an id
        itemId = getIntent().getIntExtra("itemId", -1);
        if (itemId != -1) {
            setTitle("Edit Ingredient");
            buttonDelete.setVisibility(View.VISIBLE);
            loadItem();
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    saveIngredient();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteIngredient();
            }
        });
    }

    // gets the item from the database and fills the boxes
    private void loadItem() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                currentItem = AppDatabase.getInstance(AddEditIngredientActivity.this)
                        .pantryItemDao().getItemById(itemId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentItem != null) {
                            editName.setText(currentItem.getName());
                            editQuantity.setText(String.valueOf(currentItem.getQuantity()));
                            editUnit.setText(currentItem.getUnit());
                            if (currentItem.getExpiryDate() != null) {
                                editExpiry.setText(currentItem.getExpiryDate());
                            }
                        }
                    }
                });
            }
        });
    }

    // adds a new item or updates the one being edited
    private void saveIngredient() {
        String name = editName.getText().toString().trim();
        double quantity = Double.parseDouble(editQuantity.getText().toString().trim());
        String unit = editUnit.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();

        // expiry is optional, so save null if it is empty
        String expiryToSave = expiry.isEmpty() ? null : expiry;

        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(AddEditIngredientActivity.this);

                if (itemId == -1) {
                    // new item
                    db.pantryItemDao().insert(new PantryItem(name, quantity, unit, expiryToSave));
                } else {
                    // existing item: change its values and update
                    currentItem.setName(name);
                    currentItem.setQuantity(quantity);
                    currentItem.setUnit(unit);
                    currentItem.setExpiryDate(expiryToSave);
                    db.pantryItemDao().update(currentItem);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddEditIngredientActivity.this,
                                "Ingredient saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    // removes the item being edited
    private void deleteIngredient() {
        if (currentItem == null) {
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(AddEditIngredientActivity.this)
                        .pantryItemDao().delete(currentItem);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddEditIngredientActivity.this,
                                "Ingredient deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }


    private boolean validateInput() {
        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();

        if (name.isEmpty()) {
            editName.setError("Please enter an ingredient name");
            editName.requestFocus();
            return false;
        }

        if (quantityText.isEmpty()) {
            editQuantity.setError("Please enter a quantity");
            editQuantity.requestFocus();
            return false;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            editQuantity.setError("Quantity must be a number");
            editQuantity.requestFocus();
            return false;
        }
        if (quantity <= 0) {
            editQuantity.setError("Quantity must be greater than 0");
            editQuantity.requestFocus();
            return false;
        }

        if (unit.isEmpty()) {
            editUnit.setError("Please enter a unit (e.g. g, ml, pcs)");
            editUnit.requestFocus();
            return false;
        }

        if (!expiry.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            format.setLenient(false);
            try {
                format.parse(expiry);
            } catch (ParseException e) {
                editExpiry.setError("Use the format dd-MM-yyyy");
                editExpiry.requestFocus();
                return false;
            }
        }

        return true;
    }
}