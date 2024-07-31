package com.cs2340aG49.greenPlate.ui.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs2340aG49.greenPlate.databinding.ActivityInputMealBinding;
import com.cs2340aG49.greenPlate.ui.NavigationBar;
import com.cs2340aG49.greenPlate.ui.model.Database;
import com.cs2340aG49.greenPlate.ui.model.Meal;
import com.cs2340aG49.greenPlate.ui.model.User;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.util.ArrayList;
import java.util.List;

public class InputMealActivity extends NavigationBar {

    private ActivityInputMealBinding activityInputMealBinding;
    private Database database;
    private EditText mealNameEditText;
    private EditText calorieCountEditText;
    private Button inputMealButton;
    private TextView welcomeUser;
    private TextView userData;
    private TextView calorieGoal;
    private TextView currentCalories;
    private AnyChartView dataVis;
    private Button showDataVis1Button;
    private Button showDataVis2Button;
    private Button hideDataVisButton;
    private String userName;
    private User user;
    private double goalCal;

    private Cartesian cartesian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityInputMealBinding = activityInputMealBinding.inflate(getLayoutInflater());
        setContentView(activityInputMealBinding.getRoot());
        allocatedActivityTitle("Input Meal");
        userName = NavigationBar.getUsername();
        goalCal = 0;

        mealNameEditText = activityInputMealBinding.mealNameEditText;
        calorieCountEditText = activityInputMealBinding.calorieCountEditText;
        inputMealButton = activityInputMealBinding.inputMealButton;
        welcomeUser = activityInputMealBinding.welcomeuser;
        userData = activityInputMealBinding.userdata;
        calorieGoal = activityInputMealBinding.caloriegoal;
        currentCalories = activityInputMealBinding.currentcalories;
        dataVis = activityInputMealBinding.dataVis;
        showDataVis1Button = activityInputMealBinding.showDataVis1Button;
        showDataVis2Button = activityInputMealBinding.showDataVis2Button;
        hideDataVisButton = activityInputMealBinding.hideDataVisButton;
        database = Database.getInstance();

        cartesian = AnyChart.line();
        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        dataVis.setChart(cartesian);

        // FOR DATABASE ACCESS:
        // List<Meal> meals = database.getMeals(NavigationBar.getUsername());
        // database.addMeal(new Meal("mealname", calorie_count, NavigationBar.getUsername());

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                inputMealButton.setEnabled(false);

                // Validate meal properties
                if (mealNameEditText.getText().toString().equals("")) {
                    mealNameEditText.setError("meal name cannot be empty");
                } else if (calorieCountEditText.getText().toString().equals("")) {
                    calorieCountEditText.setError("calories cannot be empty");
                } else {
                    inputMealButton.setEnabled(true);
                }
            }
        };

        mealNameEditText.addTextChangedListener(watcher);
        calorieCountEditText.addTextChangedListener(watcher);
        welcomeUser.setText("Welcome, " + userName);
        updateCalories();

        try {
            user = database.getUser(userName);
            userData.setText(String.format("Height: %.1f, Weight: %.1f, Gender: %s",
                    user.getHeight(), user.getWeight(), user.getGender()
            ));
            goalCal = Math.max(calculateCalorieGoal(user.getHeight(),
                    user.getWeight(), user.getGender()), 0);
            calorieGoal.setText(String.format("Daily Calorie Goal: %.1f",
                    goalCal));
        } catch (Exception e) {
            userData.setText("Please set up your user information, " + userName);
            calorieGoal.setText("Cannot calculate calorie goal without user information.");
            goalCal = 0;
        }

        inputMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.addMeal(userName, new Meal(mealNameEditText.getText().toString(),
                        Double.parseDouble(calorieCountEditText.getText().toString())));

                double newCalories = Double.parseDouble(calorieCountEditText.getText().toString());

                mealNameEditText.setText("");
                calorieCountEditText.setText("");
                inputMealButton.setEnabled(false);

                updateCalories(newCalories);
            }
        });

        showDataVis1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataVis1();
                dataVis.setVisibility(View.VISIBLE);
                hideDataVisButton.setEnabled(true);
                showDataVis1Button.setVisibility(View.GONE);
                showDataVis2Button.setVisibility(View.GONE);
            }
        });

        showDataVis2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataVis2();
                dataVis.setVisibility(View.VISIBLE);
                hideDataVisButton.setEnabled(true);
                showDataVis1Button.setVisibility(View.GONE);
                showDataVis2Button.setVisibility(View.GONE);
            }
        });

        hideDataVisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataVis.setVisibility(View.GONE);
                hideDataVisButton.setEnabled(false);
                showDataVis1Button.setVisibility(View.VISIBLE);
                showDataVis2Button.setVisibility(View.VISIBLE);
            }
        });
    }

    private double calculateCalorieGoal(double height, double weight, String gender) {
        double reqCalories = 10 * weight + 6.25 * height - 5 * 30 + 5;
        if (gender.equals("Female")) {
            reqCalories -= 166;
        }
        return reqCalories;
    }

    public void updateCalories() {
        double currCalories = 0;
        List<Meal> meals = database.getTodayMeals(userName);
        for (Meal meal : meals) {
            currCalories += meal.getCalorieCount();
        }
        currentCalories.setText(String.format("Daily Calorie Intake: %.1f", currCalories));
    }

    public void updateCalories(double newCalories) {
        double currCalories = newCalories;
        List<Meal> meals = database.getTodayMeals(userName);
        for (Meal meal : meals) {
            currCalories += meal.getCalorieCount();
        }
        currentCalories.setText(String.format("Daily Calorie Intake: %.1f", currCalories));
    }

    public void setDataVis1() {

        cartesian.removeAllSeries();

        cartesian.title("Daily Caloric Intake");

        cartesian.yAxis(0).title("Calories");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();

        List<Meal> meals = database.getMeals(userName);
        for (Meal meal : meals) {
            seriesData.add(new ValueDataEntry(meal.getDate().toString(), meal.getCalorieCount()));
        }


        Line series1 = cartesian.line(seriesData);
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
    }

    public void setDataVis2() {

        cartesian.removeAllSeries();

        cartesian.title("Intake vs Goal");

        cartesian.yAxis(0).title("Intake - Goal");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();

        List<Meal> meals = database.getMeals(userName);
        int currDate = meals.size() > 0 ? meals.get(0).getDate().getDate() : 0;
        double dayCal = 0;

        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getDate().getDate() == currDate) {
                dayCal += meals.get(i).getCalorieCount();
            } else {
                seriesData.add(new ValueDataEntry(currDate, dayCal - goalCal));
                currDate = meals.get(i).getDate().getDate();
                dayCal = meals.get(i).getCalorieCount();
            }
        }
        if (meals.size() > 0) {
            seriesData.add(new ValueDataEntry(currDate, dayCal - goalCal));
        }

        Line series1 = cartesian.line(seriesData);
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
    }

}