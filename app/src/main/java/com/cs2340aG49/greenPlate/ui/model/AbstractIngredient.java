package com.cs2340aG49.greenPlate.ui.model;

import androidx.annotation.NonNull;

import java.util.Objects;
public abstract class AbstractIngredient {
    protected String ingredientName;
    protected int ingredientCount;
    protected int ingredientCalories;

    protected AbstractIngredient(String ingredientName, int ingredientCount) {
        this.ingredientName = ingredientName;
        this.ingredientCount = ingredientCount;
        this.ingredientCalories = 0;
    }

    protected AbstractIngredient(String ingredientName, int ingredientCount,
                                 int ingredientCalories) {
        this.ingredientName = ingredientName;
        this.ingredientCount = ingredientCount;
        this.ingredientCalories = ingredientCalories;
    }


    public AbstractIngredient() {
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getIngredientCount() {
        return ingredientCount;
    }

    public void setIngredientCount(int ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    public int getIngredientCalories() {
        return ingredientCalories;
    }

    public int getTotalCalories() {
        return ingredientCalories * ingredientCount;
    }

    public void setIngredientCalories(int ingredientCalories) {
        this.ingredientCalories = ingredientCalories;
    }

    @NonNull
    @Override
    public String toString() {
        return "Ingredient{"
                + "ingredientName='" + ingredientName + '\''
                + ", ingredientCount=" + ingredientCount
                + '}';
    }

    public String displayIngredient() {
        return ingredientName + ", " + ingredientCount + ", " + ingredientCalories + '\n';
    }

    public String displayIngredientsDetailed() {
        return ingredientName + ", Count: " + ingredientCount
                + ", Calories: " + ingredientCalories + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof AbstractIngredient)
                || !(this instanceof AbstractIngredient)) {
            return false;
        }
        AbstractIngredient ingredient = (AbstractIngredient) o;
        return ingredient.ingredientCount == ingredientCount
                && Objects.equals(ingredientName, ingredient.ingredientName)
                && ingredient.ingredientCalories == ingredientCalories;
    }

    public boolean sameIngredient(AbstractIngredient o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        return Objects.equals(ingredientName, o.ingredientName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientName, ingredientCount);
    }
}
