package com.cs2340aG49.greenPlate.ui.model;

import java.util.List;

public class Recipe {
    private List<AbstractIngredient> ingredients;
    private String name;

    private String instructions;

    private String user;

    public Recipe(List<AbstractIngredient> ingredients, String name,
                  String instructions, String user) {
        this.ingredients = ingredients;
        this.name = name;
        this.instructions = instructions;
        this.user = user;
    }

    public Recipe(List<AbstractIngredient> ingredients, String name) {
        this.ingredients = ingredients;
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<AbstractIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<AbstractIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        StringBuilder ingreds = new StringBuilder();
        int calorieCount = 0;
        for (AbstractIngredient ingredient : ingredients) {
            ingreds.append(ingredient.displayIngredientsDetailed());
            calorieCount += ingredient.ingredientCalories * ingredient.ingredientCount;
        }

        return "Name: " + name + "\nCreator: " + user + "\nIngredients: \n"
                + ingreds + "\nTotal Calories: " + calorieCount;
    }

    private boolean sameIngredients(Recipe r) {
        return name.equals(r.name) && user.equals(r.user) && ingredients.containsAll(r.ingredients)
                && r.ingredients.containsAll(ingredients);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Recipe && sameIngredients((Recipe) other);
    }

}
