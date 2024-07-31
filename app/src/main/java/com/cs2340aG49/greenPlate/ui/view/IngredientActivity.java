package com.cs2340aG49.greenPlate.ui.view;


import android.content.Context;
import android.os.Bundle;
import android.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.text.TextWatcher;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.List;

import com.cs2340aG49.greenPlate.ui.model.AbstractIngredient;
import com.cs2340aG49.greenPlate.ui.model.Database;
import com.cs2340aG49.greenPlate.ui.model.DefaultIngredient;
import com.cs2340aG49.greenPlate.ui.model.ExpirableIngredient;
import com.cs2340aG49.greenPlate.ui.model.Pantry;


import com.cs2340aG49.greenPlate.R;
import com.cs2340aG49.greenPlate.databinding.ActivityIngredientBinding;
import com.cs2340aG49.greenPlate.ui.NavigationBar;
import com.cs2340aG49.greenPlate.ui.model.User;


public class IngredientActivity extends NavigationBar implements DatabaseObserver {

    private ActivityIngredientBinding activityIngredientBinding;

    private Database database;
    private User user;

    private String userName;



    //Button to display the form entry to add an ingredient
    private Button displayIngredientFormButton;

    //Variables used within the form entry
    private EditText ingredientName;
    private EditText ingredientQuantity;
    private EditText ingredientCalories;
    private DatePicker ingredientExpirationDatePicker;
    private LocalDate ingredientExpirationDate;
    private CheckBox expirationCheckbox;
    private Button addIngredientButton;
    private AbstractIngredient newIngredient;
    private ListView ingredientListView;
    private CustomAdapter listViewAdapter;
    private Pantry pantry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activityIngredientBinding = activityIngredientBinding.inflate(getLayoutInflater());
        setContentView(activityIngredientBinding.getRoot());
        allocatedActivityTitle("Ingredients");

        displayIngredientFormButton = activityIngredientBinding.displayIngredientFormButton;
        ingredientListView = activityIngredientBinding.ingredientListView;

        database = Database.getInstance();
        userName = NavigationBar.getUsername();
        try {
            user = database.getUser(userName);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        }

        user = database.getUser(userName);
        pantry = database.getPantry(userName);

        listViewAdapter = new CustomAdapter(getApplicationContext(), pantry.getIngredients());
        ingredientListView.setAdapter(listViewAdapter);

        displayIngredientFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormEntry();
            }
        });

        database.addObserver(this);
    }

    //Method creates Form Entry and takes in information about ingredient
    private void showFormEntry() {
        //Create AlertDialog popup
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_ingredient_form, null);
        dialogBuilder.setView(dialogView);

        //User Inputted Information
        ingredientName = dialogView.findViewById(R.id.ingredientName);
        ingredientQuantity = dialogView.findViewById(R.id.ingredientQuantity);
        ingredientCalories = dialogView
                .findViewById(R.id.ingredientCalories);
        ingredientExpirationDatePicker = dialogView
                .findViewById(R.id.ingredientExpirationDatePicker);
        addIngredientButton = dialogView.findViewById(R.id.addIngredientButton);
        expirationCheckbox = dialogView.findViewById(R.id.expirationCheckbox);
        addIngredientButton.setEnabled(false);

        //Checkbox determines whether User chooses to input Expiration Date or not
        expirationCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the checkbox is checked, show the DatePicker
                    ingredientExpirationDatePicker.setVisibility(View.VISIBLE);

                    // Extract the selected date from DatePicker
                    int day = ingredientExpirationDatePicker.getDayOfMonth();
                    int month = ingredientExpirationDatePicker.getMonth() + 1;
                    int year = ingredientExpirationDatePicker.getYear();

                    ingredientExpirationDate = LocalDate.of(year, month, day);

                } else {
                    // If the checkbox is unchecked, hide the DatePicker and set ExpirationDate to
                    // null
                    ingredientExpirationDatePicker.setVisibility(View.GONE);
                    ingredientExpirationDate = null;
                }
            }
        });

        //Check whether User correctly filled out the input fields
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                addIngredientButton.setEnabled(false);
                boolean duplicateFound = false;
                //Create Temporary Ingredient just to check if same ingredient name
                // already in pantry
                AbstractIngredient tempIngredient = new DefaultIngredient(
                        ingredientName.getText().toString(), 0);
                List<AbstractIngredient> ingredients = pantry.getIngredients();
                for (AbstractIngredient ingredient: ingredients) {
                    if (ingredient.sameIngredient(tempIngredient)) {
                        duplicateFound = true;
                    }
                }
                //Make Sure that Ingredient Name, Quantity, and Calories Per Serving are Declared
                if (ingredientName.getText().toString().equals("")) {
                    ingredientName.setError("name cannot be empty");
                } else if (ingredientQuantity.getText().toString().equals("")) {
                    ingredientQuantity.setError("quantity cannot be empty");
                } else if (ingredientCalories.getText().toString().equals("")) {
                    ingredientCalories.setError("calories cannot be empty");
                } else if (ingredientQuantity.getText().toString().substring(0, 1).equals("-")) {
                    //Make sure that quantity entered is not negative
                    ingredientQuantity.setError("quantity cannot be negative");
                }  else if (Integer.parseInt(ingredientQuantity.getText().toString()) == 0) {
                    //Make sure that quantity entered is not zero
                    ingredientQuantity.setError("quantity cannot be zero");
                } else if (duplicateFound) {
                    ingredientName.setError("cannot have duplicate ingredients");
                } else {
                    addIngredientButton.setEnabled(true);
                }
            }
        };

        ingredientName.addTextChangedListener(watcher);
        ingredientQuantity.addTextChangedListener(watcher);
        ingredientCalories.addTextChangedListener(watcher);


        AlertDialog alertDialog = dialogBuilder.create();
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Extract the Name, Quantity, and Calories Per Serving

                String ingredientNameValue = ingredientName.getText().toString();

                int ingredientQuantityValue = Integer.parseInt(ingredientQuantity
                        .getText().toString());

                int ingredientCalorieValue = Integer.parseInt(ingredientCalories
                        .getText().toString());

                //Connect Ingredient Information to Ingredient and Pantry
                if (ingredientExpirationDate == null) {
                    newIngredient = new DefaultIngredient(ingredientNameValue,
                            ingredientQuantityValue, ingredientCalorieValue);
                } else {
                    // R-extract the selected date from DatePicker in Case of Any Changes
                    int day = ingredientExpirationDatePicker.getDayOfMonth();
                    int month = ingredientExpirationDatePicker.getMonth() + 1;
                    int year = ingredientExpirationDatePicker.getYear();
                    ingredientExpirationDate = LocalDate.of(year, month, day);
                    newIngredient = new ExpirableIngredient(ingredientNameValue,
                            ingredientQuantityValue, ingredientCalorieValue,
                            ingredientExpirationDate);
                }

                //reset ingredient expiration date to null
                ingredientExpirationDate = null;

                pantry.addIngredient(newIngredient);
                listViewAdapter.notifyDataSetChanged();

                database.addPantry(pantry);

                //Once ingredient added -> close popup
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        alertDialog.getWindow().setLayout(800, 1600);

    }

    @Override
    public void update() {
        listViewAdapter.notifyDataSetChanged();
    }

    private class CustomAdapter extends BaseAdapter {
        private Context context;
        private List<AbstractIngredient> ingredients;
        private LayoutInflater inflater;

        public CustomAdapter(Context applicationContext, List<AbstractIngredient> ingredients) {
            this.context = applicationContext;
            this.ingredients = ingredients;
            inflater = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.activity_ingredient_list_item, null);
            TextView ingredientName = (TextView) view.findViewById(R.id.ingredientName);
            TextView ingredientCalories = (TextView) view.findViewById(R.id.ingredientCalories);
            EditText ingredientCount = (EditText) view.findViewById((R.id.ingredientCount));
            TextView expirationDate = (TextView) view.findViewById((R.id.expirationDate));

            AbstractIngredient ingredient = ingredients.get(i);

            ingredientName.setText(ingredient.getIngredientName());
            ingredientCalories.setText("" + ingredient.getIngredientCalories());
            ingredientCount.setText("" + ingredient.getIngredientCount());

            if (ingredient instanceof ExpirableIngredient) {
                expirationDate.setText("Expires: "
                        + ((ExpirableIngredient) ingredient).getIngredientExpirationDate());
            }

            ingredientCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int prevCount = ingredient.getIngredientCount();
                        int count = (int) Double.parseDouble(ingredientCount.getText().toString());

                        if (prevCount == count) {
                            return;
                        }

                        if (count > 0) {
                            ingredient.setIngredientCount(count);
                            database.addPantry(pantry);
                        } else {
                            database.removeIngredient(ingredient.getIngredientName(),
                                    pantry.getUser());
                            ingredients.remove(ingredient);
                        }

                        listViewAdapter.notifyDataSetChanged();
                    }
                }
            });

            return view;
        }
    }
}