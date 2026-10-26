package com.example.smartpantrymanager;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavHelper {


    public static void setup(final Activity activity, final int selectedId) {
        BottomNavigationView nav = activity.findViewById(R.id.bottomNav);


        nav.setSelectedItemId(selectedId);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                if (id == selectedId) {
                    return true;
                }

                Intent intent;
                if (id == R.id.nav_pantry) {
                    intent = new Intent(activity, MainActivity.class);
                    // go back to the pantry screen instead of opening a new one
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                } else if (id == R.id.nav_suggested) {
                    intent = new Intent(activity, SuggestedRecipesActivity.class);
                } else {
                    intent = new Intent(activity, SettingsActivity.class);
                }

                activity.startActivity(intent);

                // close this screen, except the pantry screen which stays as the home screen
                if (!(activity instanceof MainActivity)) {
                    activity.finish();
                }
                return true;
            }
        });
    }
}