package com.example.smartpantrymanager.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    // add a recipe and give back its new id
    @Insert
    long insertRecipe(Recipe recipe);

    // add one ingredient line of a recipe
    @Insert
    void insertIngredient(RecipeIngredient ingredient);

    // all recipes, A to Z
    @Query("SELECT * FROM recipes ORDER BY name ASC")
    List<Recipe> getAllRecipes();

    // one recipe by id
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    Recipe getRecipeById(int recipeId);

    // the ingredients that one recipe needs
    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    List<RecipeIngredient> getIngredientsForRecipe(int recipeId);

    // how many recipes there are
    @Query("SELECT COUNT(*) FROM recipes")
    int getRecipeCount();
}