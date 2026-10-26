package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.data.AppDatabase;
import com.example.smartpantrymanager.data.PantryItem;
import com.example.smartpantrymanager.data.Recipe;
import com.example.smartpantrymanager.logic.RecipeMatcher;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Intent;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerRecipes;
    private TextView textEmpty;
    private RecipeAdapter adapter;

    // database work runs in the background
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        NavHelper.setup(this, R.id.nav_suggested);

        recyclerRecipes = findViewById(R.id.recyclerRecipes);
        textEmpty = findViewById(R.id.textEmpty);

        // set up the list
        recyclerRecipes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter();
        recyclerRecipes.setAdapter(adapter);

        // when a recipe is tapped, open its detail screen and send the id
        adapter.setOnRecipeClickListener(new RecipeAdapter.OnRecipeClickListener() {
            @Override
            public void onRecipeClick(Recipe recipe) {
                Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
                intent.putExtra("recipeId", recipe.getId());
                startActivity(intent);
            }
        });
    }

    // runs every time the screen is shown, so the list is always up to date
    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestions();
    }

    // It then gets the pantry, runs the matcher and then show the result
    private void loadSuggestions() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(SuggestedRecipesActivity.this);

                List<PantryItem> pantry = db.pantryItemDao().getAllItems();
                final List<Recipe> suggested =
                        RecipeMatcher.findSuggestedRecipes(db.recipeDao(), pantry);

                // back on the main thread to update the screen
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setRecipes(suggested);

                        // show the message if nothing matches
                        if (suggested.isEmpty()) {
                            textEmpty.setVisibility(View.VISIBLE);
                            recyclerRecipes.setVisibility(View.INVISIBLE);
                        } else {
                            textEmpty.setVisibility(View.GONE);
                            recyclerRecipes.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }
}