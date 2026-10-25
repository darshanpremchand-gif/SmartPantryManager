package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantrymanager.data.AppDatabase;
import com.example.smartpantrymanager.data.Recipe;
import com.example.smartpantrymanager.data.RecipeIngredient;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textName, textIngredients, textSteps;


    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        textName = findViewById(R.id.textDetailName);
        textIngredients = findViewById(R.id.textDetailIngredients);
        textSteps = findViewById(R.id.textDetailSteps);

        // get the recipe id sent from the previous screen
        int recipeId = getIntent().getIntExtra("recipeId", -1);
        if (recipeId != -1) {
            loadRecipe(recipeId);
        }
    }

    // load the recipe and its ingredients, then show them
    private void loadRecipe(int recipeId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(RecipeDetailActivity.this);
                final Recipe recipe = db.recipeDao().getRecipeById(recipeId);
                List<RecipeIngredient> ingredients =
                        db.recipeDao().getIngredientsForRecipe(recipeId);

                // build the ingredient list, one per line
                final StringBuilder list = new StringBuilder();
                for (RecipeIngredient ingredient : ingredients) {
                    list.append("- ")
                            .append(ingredient.getName())
                            .append(": ")
                            .append(ingredient.getQuantity())
                            .append(" ")
                            .append(ingredient.getUnit())
                            .append("\n");
                }

                // back on the main thread to update the screen
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (recipe != null) {
                            textName.setText(recipe.getName());
                            textIngredients.setText(list.toString().trim());
                            textSteps.setText(recipe.getSteps());
                        }
                    }
                });
            }
        });
    }
}