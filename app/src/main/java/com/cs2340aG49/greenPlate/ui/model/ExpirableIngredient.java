package com.cs2340aG49.greenPlate.ui.model;

import java.time.LocalDate;


public class ExpirableIngredient extends AbstractIngredient {
    private LocalDate ingredientExpirationDate;

    public ExpirableIngredient(String ingredientName, int ingredientCount) {
        super(ingredientName, ingredientCount);
        this.ingredientExpirationDate = null;
    }

    public ExpirableIngredient(String ingredientName, int ingredientCount, int ingredientCalories) {
        super(ingredientName, ingredientCount, ingredientCalories);
        ingredientExpirationDate = null;
    }

    public ExpirableIngredient(String ingredientName, int ingredientCount, int ingredientCalories,
                      LocalDate ingredientExpirationDate) {
        super(ingredientName, ingredientCount, ingredientCalories);
        this.ingredientExpirationDate = ingredientExpirationDate;

    }

    public ExpirableIngredient() {
        super();
    }

    public LocalDate getIngredientExpirationDate() {
        return ingredientExpirationDate;
    }

    public void setIngredientExpirationDate(LocalDate ingredientExpirationDate) {
        this.ingredientExpirationDate = ingredientExpirationDate;
    }

    public boolean hasExpirationDate() {
        return (this.ingredientExpirationDate != null);
    }

}
