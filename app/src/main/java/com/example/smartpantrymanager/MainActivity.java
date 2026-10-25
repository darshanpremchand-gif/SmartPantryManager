package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.data.AppDatabase;
import com.example.smartpantrymanager.data.PantryItem;
import com.example.smartpantrymanager.data.RecipeSeeder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerPantry;
    private PantryAdapter adapter;


    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerPantry = findViewById(R.id.recyclerPantry);
        recyclerPantry.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter();
        recyclerPantry.setAdapter(adapter);


        adapter.setOnItemClickListener(new PantryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PantryItem item) {
                Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
                intent.putExtra("itemId", item.getId());
                startActivity(intent);
            }
        });


        Button buttonAdd = findViewById(R.id.buttonAddIngredient);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
                startActivity(intent);
            }
        });


        Button buttonSuggested = findViewById(R.id.buttonSuggested);
        buttonSuggested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
                startActivity(intent);
            }
        });


        executor.execute(new Runnable() {
            @Override
            public void run() {
                RecipeSeeder.seedIfEmpty(AppDatabase.getInstance(MainActivity.this));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }


    private void loadItems() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<PantryItem> items = AppDatabase.getInstance(MainActivity.this)
                        .pantryItemDao().getAllItems();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItems(items);
                    }
                });
            }
        });
    }
}
