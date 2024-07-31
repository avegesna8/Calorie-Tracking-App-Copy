package com.cs2340aG49.greenPlate.ui.model;

import java.util.ArrayList;
import java.util.List;

public class Pantry {
    protected List<AbstractIngredient> ingredients;
    protected String user;

    public Pantry(String user) {
        this.user = user;
        ingredients = new ArrayList<>();
    }

    public String getUser() {
        return user;
    }

    public void addIngredient(AbstractIngredient i) {
        ingredients.add(i);
    }

    public boolean removeIngredient(AbstractIngredient i) {
        return ingredients.remove(i);
    }

    public AbstractIngredient removeIngredient(int i) {
        return ingredients.remove(i);
    }

    public List<AbstractIngredient> getIngredients() {
        return ingredients;
    }

    public AbstractIngredient getIngredient(String ingredientName) {
        for (AbstractIngredient ingredient : ingredients) {
            if (ingredient.getIngredientName().equals(ingredientName)) {
                return ingredient;
            }
        }
        return null;
    }

    public boolean containsIngredient(String ingredientName) {
        for (AbstractIngredient ingredient : ingredients) {
            if (ingredient.getIngredientName().equals(ingredientName)) {
                return true;
            }
        }
        return false;
    }

    public int getIngredientCount(String ingredientName) {
        for (AbstractIngredient ingredient : ingredients) {
            if (ingredient.getIngredientName().equals(ingredientName)) {
                return ingredient.getIngredientCount();
            }
        }

        return 0;
    }

    public AbstractIngredient getIngredient(int i) {
        return ingredients.get(i);
    }

}
