package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText editName, editQuantity, editUnit, editExpiry;
    private Button buttonSave;

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
                // Only continue if every check passes
                if (validateInput()) {
                    Toast.makeText(AddEditIngredientActivity.this,
                            "Input is valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Checks each box. Returns true only if everything is OK.
    private boolean validateInput() {
        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();

        // 1. Name must not be empty
        if (name.isEmpty()) {
            editName.setError("Please enter an ingredient name");
            editName.requestFocus();
            return false;
        }

        // 2. Quantity must be a number greater than 0
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

        // 3. Unit must not be empty
        if (unit.isEmpty()) {
            editUnit.setError("Please enter a unit (e.g. g, ml, pcs)");
            editUnit.requestFocus();
            return false;
        }

        // 4. Expiry is optional, but if typed it must look like dd-MM-yyyy
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