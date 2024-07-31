package com.cs2340aG49.greenPlate.ui.model;

public abstract class AbstractIngredientFactory {
    public abstract AbstractIngredient makeIngredient(String ingredientName, int count, int value);
}


