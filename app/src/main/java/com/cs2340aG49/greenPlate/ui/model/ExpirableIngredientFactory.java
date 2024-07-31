package com.cs2340aG49.greenPlate.ui.model;

import java.time.LocalDate;

public class ExpirableIngredientFactory extends AbstractIngredientFactory {
    @Override
    public ExpirableIngredient makeIngredient(String ingredientName, int count, int calories) {
        return new ExpirableIngredient(ingredientName, count, calories);
    }

    public ExpirableIngredient makeIngredient(String ingredientName, int count, int calories,
                                              LocalDate expirationDate) {
        return new ExpirableIngredient(ingredientName, count, calories, expirationDate);
    }
}
