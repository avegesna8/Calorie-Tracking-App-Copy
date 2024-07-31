package com.cs2340aG49.greenPlate.ui.model.recipeSorting;

import com.cs2340aG49.greenPlate.ui.model.AbstractIngredient;
import com.cs2340aG49.greenPlate.ui.model.Database;
import com.cs2340aG49.greenPlate.ui.model.ExpirableIngredient;
import com.cs2340aG49.greenPlate.ui.model.Pantry;
import com.cs2340aG49.greenPlate.ui.model.Recipe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecipeFilterAvailable implements RecipeFilter {
    @Override
    public List<Recipe> filterRecipes(List<Recipe> recipes, String username) {
        List<Recipe> possibleRecipes = new ArrayList<>();
        Database database = Database.getInstance();
        Pantry ingredients = database.getPantry(username);

        for (Recipe recipe : recipes) {
            boolean recipePossible = true;
            for (AbstractIngredient recipeIngredient : recipe.getIngredients()) {
                AbstractIngredient pantryIngredient = ingredients.getIngredient(
                        recipeIngredient.getIngredientName());
                if (pantryIngredient == null) {
                    recipePossible = false;
                    break;
                }
                if (pantryIngredient instanceof ExpirableIngredient) {
                    if (((ExpirableIngredient) pantryIngredient).
                            getIngredientExpirationDate().isBefore(LocalDate.now())) {
                        recipePossible = false;
                        break;
                    }
                }
                if (pantryIngredient.getIngredientCount() < recipeIngredient.getIngredientCount()) {
                    recipePossible = false;
                    break;
                }
            }
            if (recipePossible) {
                possibleRecipes.add(recipe);
            }
        }

        return possibleRecipes;
    }
}
