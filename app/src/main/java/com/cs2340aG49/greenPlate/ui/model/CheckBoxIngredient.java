package com.cs2340aG49.greenPlate.ui.model;

public class CheckBoxIngredient extends AbstractIngredient {
    private boolean checked;
    public CheckBoxIngredient(String ingredientName, int ingredientCount) {
        super(ingredientName, ingredientCount);
        checked = false;
    }
    public CheckBoxIngredient() {
        super();
        checked = false;
    }
    public CheckBoxIngredient(String ingredientName, int ingredientCount,
                              int ingredientCalories, boolean checked) {
        super(ingredientName, ingredientCount, ingredientCalories);
        this.checked = checked;
    }

    public boolean getChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}