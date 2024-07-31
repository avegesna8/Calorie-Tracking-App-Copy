package com.cs2340aG49.greenPlate.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.cs2340aG49.greenPlate.R;
import com.cs2340aG49.greenPlate.ui.view.HomeActivity;
import com.cs2340aG49.greenPlate.ui.view.InputMealActivity;
import com.cs2340aG49.greenPlate.ui.view.RecipeActivity;
import com.cs2340aG49.greenPlate.ui.view.IngredientActivity;
import com.cs2340aG49.greenPlate.ui.view.ShoppingListActivity;
import com.cs2340aG49.greenPlate.ui.view.PersonalInfoActivity;
import com.google.android.material.navigation.NavigationView;

public class NavigationBar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private static String username = "";

    public static String getUsername() {
        return username;
    }
    public static void setUsername(String username) {
        NavigationBar.username = username;
    }

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater()
                .inflate(R.layout.activity_navigation_bar, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_bar_open, R.string.nav_bar_closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.nav_home) {
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(0, 0);
        } else if (item.getItemId() == R.id.nav_shoppinglist) {
            startActivity(new Intent(this, ShoppingListActivity.class));
        } else if (item.getItemId() == R.id.nav_inputMeal) {
            startActivity(new Intent(this, InputMealActivity.class));
            overridePendingTransition(0, 0);
        } else if (item.getItemId() == R.id.nav_recipe) {
            startActivity(new Intent(this, RecipeActivity.class));
            overridePendingTransition(0, 0);
        } else if (item.getItemId() == R.id.nav_ingredient) {
            startActivity(new Intent(this, IngredientActivity.class));
            overridePendingTransition(0, 0);
        } else if (item.getItemId() == R.id.nav_personalinfo) {
            startActivity(new Intent(this, PersonalInfoActivity.class));
            overridePendingTransition(0, 0);
        }
        return false;
    }

    protected void allocatedActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }
}