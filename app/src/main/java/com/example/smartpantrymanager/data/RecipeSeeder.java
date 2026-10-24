package com.example.smartpantrymanager.data;

public class RecipeSeeder {

    // adding the starter recipes
    public static void seedIfEmpty(AppDatabase db) {
        RecipeDao dao = db.recipeDao();

        if (dao.getRecipeCount() > 0) {
            return;
        }

        add(dao, "Scrambled Eggs",
                "1. Beat the eggs in a bowl.\n2. Melt the butter in a pan.\n3. Add the eggs and stir until cooked.\n4. Season with salt.",
                ing("egg", 3, "pcs"), ing("butter", 10, "g"), ing("salt", 1, "g"));

        add(dao, "Cheese Omelette",
                "1. Beat the eggs with a pinch of salt.\n2. Heat the butter and pour in the eggs.\n3. Add the cheese on top.\n4. Fold over and cook until the cheese melts.",
                ing("egg", 3, "pcs"), ing("cheese", 50, "g"), ing("butter", 10, "g"), ing("salt", 1, "g"));

        add(dao, "Tomato Pasta",
                "1. Cook the pasta and drain it.\n2. Fry the onion and garlic.\n3. Add the tomatoes and cook until soft.\n4. Mix in the pasta and add salt.",
                ing("pasta", 200, "g"), ing("tomato", 4, "pcs"), ing("onion", 1, "pcs"),
                ing("garlic", 2, "pcs"), ing("salt", 1, "g"));

        add(dao, "Garlic Butter Pasta",
                "1. Cook the pasta and drain it.\n2. Melt the butter and fry the garlic.\n3. Mix the pasta into the pan.\n4. Add salt.",
                ing("pasta", 200, "g"), ing("butter", 30, "g"), ing("garlic", 3, "pcs"), ing("salt", 1, "g"));

        add(dao, "Cheese Pasta",
                "1. Cook the pasta and drain it.\n2. Warm the milk and butter in a pot.\n3. Stir in the cheese until it melts.\n4. Add the pasta and mix.",
                ing("pasta", 200, "g"), ing("cheese", 100, "g"), ing("butter", 20, "g"), ing("milk", 100, "ml"));

        add(dao, "Cheese Toastie",
                "1. Butter one side of each slice of bread.\n2. Put the cheese between the slices.\n3. Fry until golden on both sides.",
                ing("bread", 2, "pcs"), ing("cheese", 60, "g"), ing("butter", 10, "g"));

        add(dao, "Egg Fried Rice",
                "1. Fry the onion in the oil.\n2. Move it to the side and scramble the eggs.\n3. Add the cooked rice and stir.\n4. Season with salt.",
                ing("rice", 200, "g"), ing("egg", 2, "pcs"), ing("onion", 1, "pcs"),
                ing("oil", 20, "ml"), ing("salt", 1, "g"));

        add(dao, "Vegetable Soup",
                "1. Cut the potatoes, carrots and onion into pieces.\n2. Put them in a pot with the water.\n3. Simmer for about 25 minutes.\n4. Add salt.",
                ing("potato", 2, "pcs"), ing("carrot", 2, "pcs"), ing("onion", 1, "pcs"),
                ing("water", 500, "ml"), ing("salt", 1, "g"));

        add(dao, "Mashed Potato",
                "1. Peel the potatoes and boil until soft.\n2. Drain and mash.\n3. Add the butter and milk and mix.\n4. Season with salt.",
                ing("potato", 4, "pcs"), ing("butter", 30, "g"), ing("milk", 100, "ml"), ing("salt", 1, "g"));

        add(dao, "Pancakes",
                "1. Mix the flour, egg and milk into a smooth batter.\n2. Melt a little butter in a pan.\n3. Pour in some batter and cook until golden on both sides.",
                ing("flour", 150, "g"), ing("egg", 1, "pcs"), ing("milk", 250, "ml"), ing("butter", 20, "g"));

        add(dao, "French Toast",
                "1. Mix the eggs and milk in a bowl.\n2. Dip the bread slices in it.\n3. Fry in butter until golden on both sides.",
                ing("bread", 4, "pcs"), ing("egg", 2, "pcs"), ing("milk", 100, "ml"), ing("butter", 10, "g"));

        add(dao, "Tomato Soup",
                "1. Chop the tomatoes, onion and garlic.\n2. Cook in a pot with the water for 20 minutes.\n3. Blend until smooth.\n4. Add salt.",
                ing("tomato", 6, "pcs"), ing("onion", 1, "pcs"), ing("garlic", 2, "pcs"),
                ing("water", 300, "ml"), ing("salt", 1, "g"));

        add(dao, "Rice and Beans",
                "1. Cook the rice.\n2. Fry the onion in the oil.\n3. Add the beans and heat them.\n4. Mix with the rice and add salt.",
                ing("rice", 200, "g"), ing("bean", 200, "g"), ing("onion", 1, "pcs"),
                ing("oil", 15, "ml"), ing("salt", 1, "g"));

        add(dao, "Chicken and Rice",
                "1. Cut the chicken into pieces.\n2. Fry the chicken and onion in the oil until brown.\n3. Add the rice and enough water to cook it.\n4. Cook until the rice is soft, then add salt.",
                ing("chicken", 300, "g"), ing("rice", 200, "g"), ing("onion", 1, "pcs"),
                ing("oil", 15, "ml"), ing("salt", 1, "g"));

        add(dao, "Potato Wedges",
                "1. Cut the potatoes into wedges.\n2. Mix them with the oil and salt.\n3. Bake at 200 degrees for 35 minutes, turning once.",
                ing("potato", 4, "pcs"), ing("oil", 30, "ml"), ing("salt", 2, "g"));

        add(dao, "Banana Pancakes",
                "1. Mash the bananas in a bowl.\n2. Mix in the eggs and flour.\n3. Cook spoonfuls in a buttered pan until golden on both sides.",
                ing("banana", 2, "pcs"), ing("egg", 2, "pcs"), ing("flour", 50, "g"), ing("butter", 10, "g"));
    }

    // saves one recipe, then saves each of its ingredient
    private static void add(RecipeDao dao, String name, String steps,
                            RecipeIngredient... ingredients) {
        long recipeId = dao.insertRecipe(new Recipe(name, steps));
        for (RecipeIngredient ingredient : ingredients) {
            ingredient.setRecipeId((int) recipeId);
            dao.insertIngredient(ingredient);
        }
    }

    // makes one ingredient line
    private static RecipeIngredient ing(String name, double quantity, String unit) {
        return new RecipeIngredient(0, name, quantity, unit);
    }
}
