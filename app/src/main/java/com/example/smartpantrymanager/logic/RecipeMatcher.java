package com.example.smartpantrymanager.logic;

import com.example.smartpantrymanager.data.PantryItem;
import com.example.smartpantrymanager.data.Recipe;
import com.example.smartpantrymanager.data.RecipeDao;
import com.example.smartpantrymanager.data.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeMatcher {

    // This returns only the recipes the user can make with what is in the pantry
    public static List<Recipe> findSuggestedRecipes(RecipeDao recipeDao, List<PantryItem> pantry) {
        Map<String, Double> available = buildAvailable(pantry);
        List<Recipe> suggested = new ArrayList<>();

        for (Recipe recipe : recipeDao.getAllRecipes()) {
            List<RecipeIngredient> needed = recipeDao.getIngredientsForRecipe(recipe.getId());
            if (canMake(needed, available)) {
                suggested.add(recipe);
            }
        }
        return suggested;
    }

    // Adds up the pantry by name and unit type, e.g. "egg|count" -> 6
    private static Map<String, Double> buildAvailable(List<PantryItem> pantry) {
        Map<String, Double> available = new HashMap<>();

        for (PantryItem item : pantry) {
            String key = makeKey(item.getName(), item.getUnit());
            double amount = amountOf(item.getQuantity(), item.getUnit());

            Double current = available.get(key);
            if (current == null) {
                available.put(key, amount);
            } else {
                available.put(key, current + amount);
            }
        }
        return available;
    }

    // the strict rule: EVERY ingredient must be there in a big enough amount
    private static boolean canMake(List<RecipeIngredient> needed, Map<String, Double> available) {
        // a recipe with no ingredients is never suggested
        if (needed.isEmpty()) {
            return false;
        }

        for (RecipeIngredient ingredient : needed) {
            String key = makeKey(ingredient.getName(), ingredient.getUnit());
            double required = amountOf(ingredient.getQuantity(), ingredient.getUnit());

            Double have = available.get(key);

            // Any missing ingredient, or not enough of it then the whole recipe fails
            if (have == null || have < required - 0.0001) {
                return false;
            }
        }
        return true;
    }

    // name + unit type, e.g. "tomato|count", so "Tomatoes" matches "tomato"
    private static String makeKey(String name, String unit) {
        return MatchUtils.normalizeName(name) + "|" + typeOf(unit);
    }

    // weight, volume or count; for a unit we don't know, use the unit text itself
    private static String typeOf(String unit) {
        String type = MatchUtils.unitType(unit);
        if (type.equals("unknown")) {
            return unit.trim().toLowerCase();
        }
        return type;
    }

    // the quantity in a base unit g, ml or pieces
    private static double amountOf(double quantity, String unit) {
        double base = MatchUtils.toBaseAmount(quantity, unit);
        if (base < 0) {
            return quantity;
        }
        return base;
    }
}