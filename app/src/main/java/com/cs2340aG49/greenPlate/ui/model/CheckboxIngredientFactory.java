package com.cs2340aG49.greenPlate.ui.model;

public class CheckboxIngredientFactory extends AbstractIngredientFactory {
    @Override
    public CheckBoxIngredient makeIngredient(String ingredientName, int count, int calories) {
        return new CheckBoxIngredient(ingredientName, count, calories, false);
    }

    public CheckBoxIngredient makeIngredient(String ingredientName, int count, boolean checked) {
        return new CheckBoxIngredient(ingredientName, count, 0, checked);
    }

    public CheckBoxIngredient makeIngredient(String ingredientName, int count) {
        return new CheckBoxIngredient(ingredientName, count, 0, false);
    }

    public CheckBoxIngredient makeIngredient(String ingredientName, int count, int calories,
                                             boolean checked) {
        return new CheckBoxIngredient(ingredientName, count, calories, checked);
    }
}
