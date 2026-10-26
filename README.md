# Smart Pantry Manager

Smart Pantry Manager is an Android app I built in Java for Mobile App Development 700. 
The idea is simple: 
You add the ingredients you already have at home, and the app shows you recipes you can make with just those, so you don't have to go shopping and less food gets thrown away.

## What the app does

- Add, edit and delete pantry items (name, quantity, unit and an optional expiry date)
- Shows your pantry in a list, and the items are still there after you close the app
- Comes with 16 recipes that are added the first time the app runs
- Suggested Recipes screen that only lists recipes you can make right now
- Recipe Detail screen with the ingredients and the method
- Settings screen where you can turn expiring-soon alerts on or off
- Bottom navigation bar to move between the main screens
- Checks the ingredient form for mistakes and shows an error message
- Shows a message when no recipes match what's in your pantry

## How the matching works

A recipe only shows up if you have every ingredient it needs, in at least the amount it needs.
If one ingredient is missing, or you don't have enough of it, the recipe is left out. I made it handle small differences too, so "Tomatoes" still matches "tomato" and "1 kg" counts the same as "1000 g".

## Database

I used SQLite through the Room library. I picked it because it runs on the phone itself, so there's nothing to set up and it works without internet.
The data stays saved when the app is closed. There are three tables: pantry_items, recipes and recipe_ingredients.

## How to run it

1. Download or clone this repository.
2. Open the folder in Android Studio.
3. Wait for Gradle to finish syncing.
4. Pick an emulator or plug in an Android phone (Android 7.0 / API 24 or higher).
5. Press the green Run button.

## Made with

- Java
- Android Studio
- Room (SQLite)
- RecyclerView with custom adapters
- Material bottom navigation
