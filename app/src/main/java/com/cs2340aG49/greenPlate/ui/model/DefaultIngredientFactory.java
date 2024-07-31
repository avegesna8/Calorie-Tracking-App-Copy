package com.cs2340aG49.greenPlate.ui.model;

public class DefaultIngredientFactory extends AbstractIngredientFactory {

    @Override
    public DefaultIngredient makeIngredient(String ingredientName, int count, int calories) {
        return new DefaultIngredient(ingredientName, count, calories);
    }
}
