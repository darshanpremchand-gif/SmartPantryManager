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
    private Button buttonSave;

    // runs database work in the background
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiry = findViewById(R.id.editExpiry);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    saveIngredient();
                }
            }
        });
    }

    // reads the boxes and saves a new item in the database
    private void saveIngredient() {
        String name = editName.getText().toString().trim();
        double quantity = Double.parseDouble(editQuantity.getText().toString().trim());
        String unit = editUnit.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();

        // expiry is optional, so save null if it is empty
        String expiryToSave = expiry.isEmpty() ? null : expiry;

        PantryItem item = new PantryItem(name, quantity, unit, expiryToSave);

        // database work must not run on the main thread
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(AddEditIngredientActivity.this)
                        .pantryItemDao().insert(item);

                // go back to the main thread to update the screen
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddEditIngredientActivity.this,
                                "Ingredient saved", Toast.LENGTH_SHORT).show();
                        finish(); // closes the form and returns to the pantry screen
                    }
                });
            }
        });
    }

    // checks every box, returns true only if all are filled
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