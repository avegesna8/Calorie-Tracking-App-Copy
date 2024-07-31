package com.cs2340aG49.greenPlate.ui.model.recipeSorting;

import com.cs2340aG49.greenPlate.ui.model.Recipe;

import java.util.List;

public class RecipeFilterDefault implements RecipeFilter {
    @Override
    public List<Recipe> filterRecipes(List<Recipe> recipes, String username) {
        return recipes;
    }
}
