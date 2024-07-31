package com.cs2340aG49.greenPlate.ui.model;

import java.util.ArrayList;
import java.util.List;

public class CookBook {
    private List<Recipe> recipeList;

    public CookBook() {
        recipeList = new ArrayList<>();
    }

    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
    }

    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipeList);
    }

    public void removeRecipe(Recipe recipe) {
        recipeList.remove(recipe);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CookBook)) {
            return false;
        }
        CookBook c = (CookBook) o;
        return c.getAllRecipes().containsAll(recipeList)
                && recipeList.containsAll(c.getAllRecipes());
    }
}
