package com.cs2340aG49.greenPlate.ui.model.recipeSorting;

import com.cs2340aG49.greenPlate.ui.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeFilterUser implements RecipeFilter {
    @Override
    public List<Recipe> filterRecipes(List<Recipe> recipes, String username) {
        List<Recipe> userRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getUser().equals(username)) {
                userRecipes.add(recipe);
            }
        }
        return userRecipes;
    }
}
