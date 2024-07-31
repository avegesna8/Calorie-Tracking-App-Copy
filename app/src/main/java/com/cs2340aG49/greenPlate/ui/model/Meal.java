package com.cs2340aG49.greenPlate.ui.model;

import androidx.annotation.NonNull;
import java.util.Date;
import java.util.Objects;


public class Meal {
    private String mealName;
    private double calorieCount;
    private Date date;

    public Meal(String mealName, double calorieCount) {
        this.mealName = mealName;
        this.calorieCount = calorieCount;
        date = new Date();
    }

    public Meal() {
    }

    public String getMealName() {
        return mealName;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public double getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(double calorieCount) {
        this.calorieCount = calorieCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "Meal{"
                + "mealName='" + mealName + '\''
                + ", calorieCount=" + calorieCount
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Meal meal = (Meal) o;
        return Double.compare(meal.calorieCount, calorieCount) == 0
                && Objects.equals(mealName, meal.mealName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealName, calorieCount);
    }
}
